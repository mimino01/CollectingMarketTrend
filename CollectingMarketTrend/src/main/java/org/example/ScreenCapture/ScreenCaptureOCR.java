package org.example.ScreenCapture;

import net.sourceforge.tess4j.Tesseract;
import org.example.Main;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;


public class ScreenCaptureOCR {
	// 캡쳐할 화면 상 하단 저장
	private static Point leftTop;
	private static Point rightBottom;
	
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
					screenCapture();
				}
			}
		});
		frame.setVisible(true);
	}
	/**
	 * 1. 화면을 캡쳐해서 이미지 파일로 저장
	 * 2. 이미지 파일을 텍스트로 추출 (OCR 사용)
	 */
	private static void screenCapture () {
		try {
			// 캡처 영역 계산
			int x = Math.min(leftTop.x, rightBottom.x);
			int y = Math.min(leftTop.y, rightBottom.y);
			int width = Math.abs(leftTop.x - rightBottom.x);
			int height = Math.abs(leftTop.y - rightBottom.y);
			
			// Robot 객채를 사용해서 화면 캡쳐
			Robot robot = new Robot();
			Rectangle screenRect = new Rectangle(x, y, width, height);
			BufferedImage screenShot = robot.createScreenCapture(screenRect);
			
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
			
			System.out.println(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
