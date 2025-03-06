package org.example.RAGCalculator;

import org.jetbrains.annotations.NotNull;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static final String partyLevel = "288";
    
    /**
     * 로스트아크 내 경제 상황 분석기
     *
     * @param args
     */
    public static void main(String[] args) throws IOException {
        // 스캐너 함수 호출
        Scanner scanner = new Scanner(System.in);
    
        while (true) {
            // 원하는 프로그램 입력받은 후 실행
            System.out.println("쌀산기 0 입력");
            System.out.println("크리저금 1 입력");
            System.out.println("그외 입력시 종료");
            /**
             * 0 입력시 goldCalculater 호출
             * 1 입력시 cristalCalculater 호출
             * 그외 입력시 프로그램 종료
             */
            switch (scanner.next()) {
                case "0":
                    goldCalculator();
                    break;
                case "1":
                    cristalCalculator();
                    break;
                case "2":
            
                default:
                    return;
            }
        }
    }
    
    /**
     * 로스트아크내 사용되는 저금통인 크리스탈 세부값 계산기
     * 골드 가격, 저금할 골드를 입력받음
     * 적정 골드가격, 크리스탈 개수를 출력받음
     */
    private static void cristalCalculator() {
        // 스캐너 호출
        Scanner scanner = new Scanner(System.in);
        // min은 최솟값, val은 현재값, amu는 현재 소유중인 골득값
        int min = 0;
        int val = 0;
        int amu = 0;
    
        // 데이터 입력받음
        System.out.println("최솟값을 입력하세요");
        min = scanner.nextInt();
        System.out.println("현재값을 입력하세요");
        val = scanner.nextInt();
        System.out.println("저금할 값을 입력하세요");
        amu = scanner.nextInt();
    
        // 데이터 무결성 검사
        if (min < 0) {
            System.out.println("최솟값이 너무 작습니다");
        } else if (min > val) {
            System.out.println("최솟값 대비 현재값이 너무 높습니다");
        } else if (val < 0) {
            System.out.println("현재값이 너무 작습니다");
        } else if (amu < 0) {
            System.out.println("계산하려는 값이 너무 작습니다");
        } else {
            /**
             * 환률 계산
             * 계산하려는 값에서 최솟값을 나눈다
             * 몫이 같은 값중 가장 큰 값으로 나눈 나머지를 구한다
             * 만약 기 저장된 값보다 작다면 기 저장된 값을 날리고 저장한다
             * 현재값에 도달할 때 까지 반복한다
             */
            int quo = 0; // 연산중인 몫
            int rem = 0; // 연산중인 나머지
            int minQuo = amu; // 나머지가 가장 작은 값의 몫
            int minRem = amu; // 나머지가 가장 작은 값
            int wayPoint = min; // 연산중인 나눌 값
            int nextPoint = min; // 몫이 같은 값중 가장 큰 웨이값
            
//            if (amu / min > min) {
//
//            }
//
//            while (true) {
//                quo = amu / wayPoint;
//                rem = amu % wayPoint;
//
//                nextPoint += rem / quo;
//                if (nextPoint > val) nextPoint = val;
//
//                if ((amu % nextPoint) < minRem) {
//                    minRem = amu % nextPoint;
//                    minQuo = amu / nextPoint;
//                }
//                wayPoint = nextPoint;
//
//                if (nextPoint >= val) {
//                    break;
//                } else {
//                    quo = amu / wayPoint - 1;
//                    rem = amu % wayPoint;
//                }
//            }
//
//            System.out.println("저금하려는 골드량 : " + amu + " 저금 되는 골드량 : " + minRem * minQuo);
//            System.out.println("저금하는데 입력하는 골드량 : " + minRem);
//            System.out.println("구매하는 크리스탈 량 : " + (minQuo * 100));
    
            /**
             * 환율 계산
             * 1. 저금하려는 값(amu) 에서 최솟값(min)을 나눈다
             * 2. 나머지에 몫을 나누고 최솟값에 더한뒤 현재가와 비교를 한다
             * 3-1. 현재가가 더 낮다면 제금하려는 값에서 현재가 *0.9를 한 값을 나눈 값을 출력
             * 3-2. 현재가가 더 높다면 저금하려는 값에서 나머지에 몫을 나누고 최솟값을 더한 값을 출력
             */
            int Quotient; // 저금하려는 값에서 최소값을 나눈 몫
            int Remainder; // 저금하려는 값에서 최소값을 나눈 나머지
            int minRemainder = amu; // 저금하려는 값에서 최소값을 나눈 나머지가 가장 작은 값
            int minQuotient = amu;
            int i = min;
            int gold = 0;
            int specialValue = 0;
    
            /**
             * 특수해 제거
             * 0. 일반적인 상황
             * 1. 최솟값의 두배가 저금하려는 값보다 크다면 최댓값이 나머지가 가장 작은 값이다.
             */
            if ((min * 2) > amu) specialValue = 1;
            switch (specialValue) {
                case 0:
                    while (true) {
                        Quotient = amu / i;
                        Remainder = amu % i;
    
                        if (Remainder < minRemainder) {
                            minRemainder = Remainder;
                            minQuotient = Quotient;
                            gold = amu - minRemainder;
                        }
                        if (minRemainder == 0) break;
                        i++;
                        if (i >= val) break;
                    }
                    break;
                case 1:
                    minQuotient = 1;
                    gold = val;
                    break;
                default:
                    break;
            }
    
            System.out.println("저금하려는 골드량 : " + amu + " 저금 되는 골드량 : " + gold);
            System.out.println("저금하는데 입력하는 골드량 : " + (gold / minQuotient));
            System.out.println("구매하는 크리스탈 량 : " + (minQuotient * 100));
        }
    }
    
    /**
     * 로스트아크 내 통용되는 재화인 골드 환율 계산기
     *
     * @throws IOException
     */
    public static void goldCalculator() throws IOException {
        Scanner DataScanner = new Scanner(System.in);
        Scanner scanner = new Scanner(System.in);
        String filePath = "src/data.txt";
    
        // 데이터 저장
        System.out.println("데이터를 입력해주세요");
        String notProcessingData = "";
        String temp = "";
    
//        while (DataScanner.hasNextLine()) {
//            temp = DataScanner.next();
//            notProcessingData += temp + "\n";
////                System.out.println("scanning data / data = " + temp);
//            if (temp.equals("봇")) {
//                break;
//            }
//        }

//            while (scanner.hasNextLine()) {
//                Scanner lineScanner = new Scanner(scanner.nextLine());
//
//                while (lineScanner.hasNext()) {
//                    temp = lineScanner.next();
//                    notProcessingData += temp + "\n";
//                }
//                lineScanner.close();
//            }


//            System.out.println("데이터 \n" + notProcessingData);
    
//        FileWriter fileWriter = new FileWriter(filePath);
//        fileWriter.write(notProcessingData);
//        fileWriter.close();

//            System.out.println("fileWriter function done");
    
        FileReader reader = new FileReader(filePath);
    
        int ch, length = 0, i = 0, sum = 0, max = 0, min = 10000000, maxLength = 0, minLength = 0;
        int[] input = new int[1000];
        double average = 0.0, resell = 0.0;

//            System.out.println("fileReader function done");
    
        // 데이터 정리
        ProData marketData = new ProData();
        while ((ch = reader.read()) != -1) {
            int a = 0;
    
            // 시간 정보 확인
//                if (ch == 8212) { // 시간 데이터 시작 지점 (-)
//                    boolean afternoon = false; // 오후면 true 오전이면 false
//                    boolean ThisDataIsTrueData = false; // 더미 데이터 검사 / 더미가아니면 true 더미면 false
//                    for (int j = 0;j < 6;j++) { // (-)로 부터 6번 뒤에 있는 것이 시간 데이터
//                        ch = reader.read();
////                        System.out.println(ch);
//                    }
////                    System.out.println("afternoon data check / data = " + ch);
//                    if (ch == 54980) { // 오후(54980) 면 true
//                        afternoon = true;
//                        ThisDataIsTrueData = true;
//                    } else if (ch == 51204) { // 오전(51204) 면  false
//                        afternoon = false;
//                        ThisDataIsTrueData = true;
//                    } else { // 더미데이터면 false
//                        ThisDataIsTrueData = false;
////                        System.out.println("wrong data insert / this '-' is wrong position");
//                    }
////                    System.out.println(ThisDataIsTrueData);
////                    System.out.println("check point 0");
//                    if (ThisDataIsTrueData) { // 더미데이터가 아니면 실행
////                    System.out.println("befor the save / data = " + ch);
//                        String timeTamp = ""; // 시간 데이터 임시 저장
////                        System.out.println("between the save / data = " + ch);
//                        if ((ch = reader.read()) == 10) { // 스페이스 다 날려 시작지점으로 이동
////                        System.out.println("saving / data = " + ch);
//                            while ((ch = reader.read()) != 10) { // 다음줄이 나오기 전까지 데이터 입력받기
////                            System.out.println("while func inside / data = " + ch);
//                                if (ch != 58) { // : 데이터 제외 다 입력
////                                System.out.println("time save data check / data = " + ch);
//                                    timeTamp = timeTamp + String.valueOf((char)ch); // 데이터 임시 저장
//                                }
//                            }
////                        System.out.println("time data check / data = " + timeTamp);
//                            time.add(Double.valueOf(timeTamp)); // 데이터 저장
////                        System.out.println("after the save");
//                        }
//                    }
//
////                    System.out.println("check point 1");
//                    while (ch != 49) { // 가격데이터 나올때 까지 이동
//                        ch = reader.read();
////                        System.out.println("shift data check / data = " + ch);
//                        // 예외 데이터 입력
//                        if (ch == 10) {
////                            System.out.println("wrong data insert");
//                            time.remove(time.size() - 1);
//                            break;
//                        }
//                    }
//
////                    System.out.println("before the price data checker / data = " + ch);
//                    // 가격정보 확인
//                    if (ch == 49) {
//                        ch = reader.read();
//                        if (ch == 48) {
//                            ch = reader.read();
//                            if (ch == 48) {
//                                ch = reader.read();
//                                while (ch == 10) ch = reader.read();
//                                if (ch == 58) {
//                                    ch = reader.read();
//                                    while (ch == 10) ch = reader.read();
//                                    a += (ch - 48) * 10;
//                                    ch = reader.read();
//                                    a += (ch - 48);
//                                    input[length] = a;
//                                    priceData.add(Double.valueOf(a));
//                                    time.add(Double.valueOf(length));
//                                    length++;
//                                }
//                            }
//                        }
//                    } else {
////                        System.out.println("wrong data insert / price data's front is not 1");
////                        time.remove(time.size() - 1);
//                    }
//                }
    
            String nowTime = ""; // 현재 처리중인 데이터 시간
            if (ch == 8212) { // 시간 데이터 시작 지점 (-)
                for (int j = 0; j < 8; j++) { // (-)로 부터 8번 뒤에 있는 것이 시간 데이터
                    ch = reader.read();
                }
                if ((ch = reader.read()) == 10) { // 스페이스 다 날려 시작지점으로 이동
                    while ((ch = reader.read()) != 10) { // 다음줄이 나오기 전까지 데이터 입력받기
                        nowTime += ch; // 시간 저장
                    }
                }
            }
    
            if (ch == 49) {
                ch = reader.read();
                if (ch == 48) {
                    ch = reader.read();
                    if (ch == 48) {
                        ch = reader.read();
                        while (ch == 10) ch = reader.read();
                        if (ch == 58) {
                            ch = reader.read();
                            while (ch == 10) ch = reader.read();
                            a += (ch - 48) * 10;
                            ch = reader.read();
                            a += (ch - 48);
                            input[length] = a;
                            marketData.setData(Double.valueOf(length), Double.valueOf(a), nowTime);
                            length++;
                        }
                    }
                }
            }
        }

//            System.out.println(Arrays.toString(input) + "\n" + Arrays.toString(priceData.toArray()) + "\n" + Arrays.toString(time.toArray()));

//            while (!(i > length)) {
//                sum += input[i];
//                if (max < input[i]) {
//                    max = input[i];
//                    maxLength = i + 1;
//                }
//                if (min > input[i] && input[i] > 1) {
//                    min = input[i];
//                    minLength = i + 1;
//                }
//                i++;
//            }
//            average = sum;
//            average /= (length);
//
//            resell = average;
//            resell *= 95;
//            resell /= 100;
//
//            marketData.timePrint();
//            marketData.numberPrint();
//            marketData.pricePrint();
    
        System.out.println(
                "\n평균값 : " + marketData.getAverage() +
                        "\n최솟값 : " + marketData.getMin()[1] + " " + marketData.getMin()[0] + " 번째줄" +
                        "\n최댓값 : " + marketData.getMax()[1] + " " + marketData.getMax()[0] + " 번째줄" +
                        "\n총 데이터 량 : " + marketData.length() + "\n리셀가 : " + marketData.getResell()
        );
    
        // 데이터 체크
//            DoubleArrayListPrinter(time);
//            DoubleArrayListPrinter(priceData);
//            DoubleArrayListLengthPrinter(time);
//            DoubleArrayListLengthPrinter(priceData);
    
        // 차트 생성
        XYChart chart = QuickChart.getChart("쌀값", "", "", "y(x)", marketData.getNumber(), marketData.getPrice());
        chart.getStyler().setYAxisMin(marketData.getMin()[1] - 5.0);
        chart.getStyler().setYAxisMax(marketData.getMax()[1] + 5.0);
        new SwingWrapper(chart).displayChart();
    
        System.out.println("판매 희망가를 입력해주세요");
    
        double price = Double.parseDouble(scanner.nextLine());
    
        System.out.println("판매 수량를 입력해주세요");
        double amount = Double.parseDouble(scanner.nextLine());
        System.out.println("100:" + (int) price + " " + (int) amount + "만골 팝니다 | 거래내역 다수 & 거래내역 2년++ | 원정대 " + partyLevel + " | 본캐대기중 | DM주세요 | 거래대기중");
        System.out.println("최종 수익금 : " + (price * amount * 100));
    }
    
    /**
     * Double 자료형으로 제작된 리스트 출력
     *
     * @param DouArr
     */
    static void DoubleArrayListPrinter(ArrayList<Double> DouArr) {
        System.out.println(DouArr.toString());
    }
    
    /**
     * Double 자료형으로 제작된 리스트 길이 출력
     *
     * @param DouArr
     */
    static void DoubleArrayListLengthPrinter(@NotNull ArrayList<Double> DouArr) {
        System.out.println("Double Array List Length / data = " + DouArr.size());
    }
    
    /**
     * goldCalculater에 사용되는 클래스
     * 시간, 가격, 고유식별번호를 가짐
     */
    static class ProData {
        
        private class DataClass {
            private String time;
            private Double price;
            private Double number;
            
            DataClass() {
                this.time = "";
                this.price = 0.0;
                this.number = 0.0;
            }
            
            DataClass(Double number, Double price, String time) {
                this.time = time;
                this.price = price;
                this.number = number;
            }
            
            public void set(Double number, Double price, String time) {
                this.time = time;
                this.price = price;
                this.number = number;
            }
            
            public String getTime() {
                return time;
            }
            
            public Double getPrice() {
                return price;
            }
            
            public Double getNumber() {
                return number;
            }
        }
        
        private ArrayList<DataClass> data;
        
        ProData() {
            data = new ArrayList<DataClass>();
        }
        
        public void setData(Double number, Double price, String time) {
            data.add(new DataClass(number, price, time));
        }
        
        public String[] getTime() {
            int i = 0;
            String[] time = new String[data.size()];
            while (data.size() > i) {
                time[i] = data.get(i).getTime();
                i++;
            }
            return time;
        }
        
        public double[] getNumber() {
            int i = 0;
            double[] arr = new double[data.size()];
            while (data.size() > i) {
                arr[i] = data.get(i).getNumber();
                i++;
            }
            return arr;
        }
        
        public double[] getPrice() {
            int i = 0;
            double[] arr = new double[data.size()];
            while (data.size() > i) {
                arr[i] = data.get(i).getPrice();
                i++;
            }
            return arr;
        }
        
        public String getAverage() {
            String average = "";
            int sum = 0;
            int amount = 0;
            while (amount < data.size()) {
                sum += data.get(amount).getPrice().intValue();
                amount++;
            }
            sum /= amount;
            average = String.valueOf(sum);
            return average;
        }
        
        public int[] getMin() {
            int[] proMin = new int[2];
            int number = 0;
            int amount = 0;
            int min = data.get(amount).getPrice().intValue();
            while (amount < data.size()) {
                if (min > data.get(amount).getPrice().intValue()) {
                    min = data.get(amount).getPrice().intValue();
                    number = data.get(amount).getNumber().intValue();
                }
                amount++;
            }
            proMin[1] = min;
            proMin[0] = number;
            return proMin;
        }
        
        public int[] getMax() {
            int[] proMax = new int[2];
            int number = 0;
            int amount = 0;
            int max = data.get(amount).getPrice().intValue();
            while (amount < data.size()) {
                if (max < data.get(amount).getPrice().intValue()) {
                    max = data.get(amount).getPrice().intValue();
                    number = data.get(amount).getNumber().intValue();
                }
                amount++;
            }
            proMax[1] = max;
            proMax[0] = number;
            return proMax;
        }
        
        public int length() {
            int length = 0;
            length = data.size();
            return length;
        }
        
        public double getResell() {
            double resell = 0;
            resell = Double.parseDouble(this.getAverage());
            resell *= 95;
            resell /= 100;
            return resell;
        }
        
        public void timePrint() {
            int i = 0;
            String[] arr = new String[data.size()];
            while (data.size() > i) {
                arr[i] = data.get(i).getTime();
                i++;
            }
            System.out.println(Arrays.toString(arr));
        }
        
        public void pricePrint() {
            int i = 0;
            double[] arr = new double[data.size()];
            while (data.size() > i) {
                arr[i] = data.get(i).getPrice();
                i++;
            }
            System.out.println(Arrays.toString(arr));
        }
        
        public void numberPrint() {
            int i = 0;
            double[] arr = new double[data.size()];
            while (data.size() > i) {
                arr[i] = data.get(i).getNumber();
                i++;
            }
            System.out.println(Arrays.toString(arr));
        }
    }
    
    /**
     * 생활 계산기 함수
     * 
      */
    static void LifeCalculator () {
        RAGCalculator Ragc = new RAGCalculator();
        
    }
}