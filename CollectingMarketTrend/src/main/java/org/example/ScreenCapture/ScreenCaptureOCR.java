package org.example.ScreenCapture;

import net.sourceforge.tess4j.Tesseract;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ScreenCaptureOCR {
	// 캡쳐할 화면 상 하단 캡처 위치 정보 스케쥴러, 데이터 저장
	private static Point leftTop;
	private static Point rightBottom;
	private static Rectangle captureRect;
	private static ScheduledExecutorService executor;
	private static String[] price;
	private static saver saverr = new saver();
	
	public static void main (String[] args) {
		SwingUtilities.invokeLater(ScreenCaptureOCR::createGUI);
	}
	
	private static void createGUI () {
		// 화면 사이즈를 가져옴
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		
		// 프레임 설정
		JFrame frame = new JFrame();
		frame.setSize(screenSize);
		frame.setUndecorated(true);
		frame.setAlwaysOnTop(true);
		frame.setOpacity(0.3f);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel label = new JLabel("캡쳐 영역 선택");
		label.setForeground(Color.RED);
		frame.add(label, BorderLayout.NORTH);
		
		// 마우스 클릭 이벤트 추가
		frame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (leftTop == null) {
					leftTop = e.getPoint();
					label.setText("우하단 선택");
				} else {
					rightBottom = e.getPoint();
					frame.dispose();
					startPeriodicCapture();
				}
			}
		});
		frame.setVisible(true);
	}
	
	private static void startPeriodicCapture () {
		try {
			// 캡처 영역 계산
			int x = Math.min(leftTop.x, rightBottom.x);
			int y = Math.min(leftTop.y, rightBottom.y);
			int width = Math.abs(leftTop.x - rightBottom.x);
			int height = Math.abs(leftTop.y - rightBottom.y);
			captureRect = new Rectangle(x, y, width, height);
			
			// 스케줄러 초기화
			executor = Executors.newSingleThreadScheduledExecutor();
			executor.scheduleAtFixedRate(() -> {
				try {
					screenCapture();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}, 0, 5, TimeUnit.SECONDS);
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 1. 화면을 캡쳐해서 이미지 파일로 저장
	 * 2. 이미지 파일을 텍스트로 추출 (OCR 사용)
	 */
	private static void screenCapture() throws Exception {
		// Robot 객채를 사용해서 화면 캡쳐
		Robot robot = new Robot();
		BufferedImage screenShot = robot.createScreenCapture(captureRect);
		
		// 캡처한 이미지를 파일로 저장
		File outputFile = new File("screenshot.png");
		ImageIO.write(screenShot, "png", outputFile);
		
		// tessrtact 로 ocr 실행
		Tesseract tesseract = new Tesseract();
		tesseract.setDatapath("src/LanguageResources");
		tesseract.setLanguage("kor+eng");
		
		// tessrtact 확인
		File tessdataDir = new File("src/LanguageResources");
		if (!tessdataDir.exists() || !tessdataDir.isDirectory()) {
			System.err.println("language Resources 폴더 인식 불가");
			return;
		}
		String result = tesseract.doOCR(outputFile);
		
//		saver saverr = new saver();
		saverr.save(result);
//		System.out.println(result);
	}
	
	private static class saver {
		private static String data;
		public saver () {
			data = "";
		}
		public static boolean compareFirstTenNonSpace(String str1, String str2) {
			// 공백 제거 후 문자열에서 앞 10글자 추출
			String filteredStr1 = str1.replaceAll("\\s+", ""); // 모든 공백 제거
			String filteredStr2 = str2.replaceAll("\\s+", "");
			
			// 10글자 미만일 경우 문자열 전체 사용
			String prefix1 = filteredStr1.length() <= 10 ? filteredStr1 : filteredStr1.substring(0, 10);
			String prefix2 = filteredStr2.length() <= 10 ? filteredStr2 : filteredStr2.substring(0, 10);
			
			// 비교
			return prefix1.equals(prefix2);
		}
		public void save(String data) {
			if (!compareFirstTenNonSpace(this.data, data)) {
				System.out.println("이전 메세지 : " + this.data);
				System.out.println("새로운 메세지 : " + data);
				this.data = data;
			} else {
				System.out.println("데이터가 같음 : " + data);
			}
		}
	}
}
