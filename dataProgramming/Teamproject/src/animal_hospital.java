import java.util.Scanner;
import java.sql.*;
import java.text.SimpleDateFormat;

public class animal_hospital {
   static boolean login=false;
   Scanner scanner = new Scanner(System.in);
   String DB_URL = "jdbc:oracle:thin:@localhost:1521:XE"; 
   String DB_USER="test";
   String DB_PASSWORD = "database";
   Statement stmt = null;
   Connection con = null; 
   ResultSet rs = null;
   CallableStatement cstmt= null;
   PreparedStatement pstmt=null;
   String callname = null;
   String id = null;
   String aName = null;
   boolean error = false;
   
   animal_hospital(){
      try {
         //JDBC 드라이버 로딍
         Class.forName("oracle.jdbc.driver.OracleDriver"); 
      }catch(ClassNotFoundException e) {
         e.printStackTrace();
      }
      try {
         con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
      }catch(SQLException e) {
         e.printStackTrace();
      }
//      stmt.close(); con.close(); rs.close(); cstmt.close(); pstmt.close();
   }
   
   public void bmi() {
         try {
               System.out.println("반려동물의 품종을 입력하세요:");
              String p_animal_species=scanner.next();
               System.out.println("반려동물의 체중을 입력하세요:");
              Double p_weight=scanner.nextDouble();
              String callname="{call height_weight(?,?,?,?)}";
              cstmt=con.prepareCall(callname);
              cstmt.setDouble(1,p_weight);
              cstmt.setString(2,p_animal_species);
              cstmt.registerOutParameter(3,java.sql.Types.DOUBLE);
              cstmt.registerOutParameter(4,java.sql.Types.VARCHAR);
               
              cstmt.execute();
              
              Double p_deviation=cstmt.getDouble(3);
              String result=cstmt.getString(4);
              
              System.out.println("평균 체중과의 편차는"+p_deviation+" 입니다." );
              System.out.println("반려동물은 "+result);
            }
            
            catch(SQLException e) {
               e.printStackTrace();
            }
      }
   
   public void printCheckUp() {
	      try{
	         int aId=0;
	         System.out.println("건강검진내역 확인하고싶은 동물 이름을 입력하시오: ");
	      aName = scanner.next();
	      System.out.println("<건강검진내역>");
	      String findA = "select animal_id from animal where mem_id = "+id+" and "
	            +"animal_name like '"+aName+"'";
	      stmt = con.createStatement();
	      rs = stmt.executeQuery(findA);
	      if(rs.next())
	         aId = rs.getInt(1);
	      
	      String findList = "select * from checkup where animal_id ="+aId;
	      stmt = con.createStatement();
	      rs = stmt.executeQuery(findList);
	      
	      while(rs.next()) {
	         error = true;
	         int checkupId = rs.getInt(1);
	         float height = rs.getFloat(2);
	         float weight = rs.getFloat(3);
	         String bloodTest = rs.getString(4);
	         String x_ray = rs.getString(5);
	         java.sql.Date chkupDate = rs.getDate(6);

	         System.out.println( "신장: "+height+", 몸무게: "+weight+", 혈액검사: "+bloodTest+", 엑스레이 검사: "+x_ray+", 검진 날짜: "+chkupDate);
	         
	      }
	      if(!error) {
	         System.out.println("조회된 데이터가 없습니다.");
	      }
	      error = false;
	   }catch(SQLException e) {e.printStackTrace();}
	   }
	   
   public void changeOrder() {
      try {

            Connection con2 = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            ResultSet rs2 = null;
            Statement stmt2 = con.createStatement();
            String changeOrder = "select order_date,amount,order_state,prod_id from orders where mem_id like '"+id+"' and "
                  + "order_state like '교환' order by order_date";
            stmt = con.createStatement();
            rs = stmt.executeQuery(changeOrder);
            System.out.println("<주문 교환 내역>");
            while(rs.next()) {
               error = true;
               java.sql.Date day = rs.getDate(1);
               int amount = rs.getInt(2);
               String orderState = rs.getString(3);
               int prodId = rs.getInt(4);
               String findProdName = "select prod_name from product where prod_id = "+prodId;
               
               stmt2 = con2.createStatement();
               rs2 = stmt2.executeQuery(findProdName);
               String prodName=null;
               if(rs2.next()) {
                  prodName = rs2.getString(1);
               }
               System.out.println("상품명: "+prodName+", 주문 개수: "+Integer.toString(amount)+"주문 상태: "+orderState+ ", 주문 날짜: "+day);
               
            }

         if(!error)
            System.out.println("조회된 데이터가 없습니다.");
         
         error = false;
      }catch(SQLException e) {e.printStackTrace();}
   }
   
   public void cancelOrder() {
      try {
            Connection con2 = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            ResultSet rs2 = null;
            Statement stmt2 = con.createStatement();
            System.out.println("\n"+id+"의 주문 취소내역을 확인합니다.\n");
            System.out.println("<주문 교환 내역>");
            String cancelOrder = "select order_date,amount,order_state,prod_id from orders where mem_id like '"+id+"' and "
                  + "order_state like '취소' order by order_date";
            stmt = con.createStatement();
            rs = stmt.executeQuery(cancelOrder);
            while(rs.next()) {
               error = true;
               java.sql.Date day = rs.getDate(1);
               int amount = rs.getInt(2);
               String orderState = rs.getString(3);
               int prodId = rs.getInt(4);
               String findProdName = "select prod_name from product where prod_id = "+prodId;
            
               stmt2 = con2.createStatement();
               rs2 = stmt2.executeQuery(findProdName);
               String prodName=null;
               if(rs2.next()) {
                  prodName = rs2.getString(1);
               }
               System.out.println("상품명: "+prodName+", 주문 개수: "+Integer.toString(amount)+"주문 상태: "+orderState+ ", 주문 날짜: "+day);
               }

         if(!error)
            System.out.println("조회된 데이터가 없습니다.");
         error = false;
      }catch(SQLException e) {e.printStackTrace();}
   }
   //내역 체크
   public void medical_details() {
      try {
    	  while(true) {
      //날짜에 따른 진료 내역 확인부터 다시
      System.out.println("진료 내역이 궁금한 동물의 이름을 입력하세요: ");
      String name = scanner.next();
      
      callname = "{call FindAnimalNo(?,?,?,?)}";
      cstmt = con.prepareCall(callname);
      cstmt.setString(1, name);
      cstmt.setString(2, id);
      cstmt.registerOutParameter(3, java.sql.Types.INTEGER);
      cstmt.registerOutParameter(4, java.sql.Types.VARCHAR);
      
      cstmt.execute();
      
      int animalId = cstmt.getInt(3);
      String result = cstmt.getString(4);
      
      if(result.equals("해당 애완 동물이 등록되어 있지 않습니다.")) {
    	  System.out.println("해당 애완 동물이 등록되어 있지 않습니다.");
    	  continue;
      } 
     
      while(result.equals("아이디 찾기 성공")) {
         System.out.println("확인을 원하는 날짜를 입력하세요:(종료 q)");
         String date = scanner.next();
         if(date.equals("q"))
            break;
         String query ="select distinct t.sickness_name, t.sickness_symptom, d.doc_name, p.medicine_name, p.medicine_effect from animal a, treatment t, doctor d, cure c, (select p.prescript_id, m.medicine_name, m.medicine_effect from medicine m, prescription p where m.prescript_id = p.prescript_id) p where c.treatment_id = t.treatment_id and d.doc_id = c.doc_id and p.prescript_id = c.prescript_id and a.animal_id = ? and c.cure_visit =?";
         pstmt = con.prepareStatement(query);
         
         pstmt.setInt(1, animalId);
         pstmt.setString(2, date);
         
         rs = pstmt.executeQuery();
         System.out.println("<날짜에 따른 진료내역 확인>");
         while(rs.next()) {
            String sickness_name = rs.getString(1);
            String sickness_symptom = rs.getString(2);
            String doc_name =rs.getString(3);
            String medicine_name = rs.getString(4);
            String medicine_effect = rs.getString(5);
            
            System.out.println("병명: "+sickness_name+", 증상: "+sickness_symptom+", 진료 의사 이름: "+doc_name+", 약 이름:"+medicine_name+", 약 효능: "+medicine_effect);
         }
         callname = "{? = call bills(?,?)}";
         cstmt = con.prepareCall(callname);
         
         cstmt.setInt(2, animalId);
         cstmt.setString(3, date);
         cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
         cstmt.executeQuery();
         
         int total = cstmt.getInt(1);
         System.out.println("----------------------------------------------------------------------");
         if (total == 0)
        	 System.out.println("진료 내역이 없습니다.");
         else
        	 System.out.println("총 금액:" +total);
      }
      break;
   }
      }catch(SQLException e) {
         e.printStackTrace();
      }
}
   
   public void register() {
      try {
         while(true) {
            System.out.println("아이디: ");
            id = scanner.next();
            callname = "{call CheckDuplicateId(?,?)}";
            cstmt = con.prepareCall(callname);
            cstmt.setString(1, id);
            cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
            cstmt.executeQuery();
            String result = cstmt.getString(2);
            while(result.equals("사용가능한 아이디입니다.")) {
               System.out.print("비밀번호: ");
               String pw = scanner.next();
               if(pw.length() >= 8)
               {
                  System.out.print("이름:");
                  String name = scanner.next();
                  System.out.print("주소:");
                  String addr = scanner.next();
                  System.out.print("연락처:");
                  String tel = scanner.next();
                  String query = "insert into members values(?,?,?,?,?)";
                  pstmt = con.prepareStatement(query);
                  pstmt.setString(1, id);
                  pstmt.setString(2, pw);
                  pstmt.setString(3, name);
                  pstmt.setString(4, addr);
                  pstmt.setString(5, tel);
                  
                  pstmt.executeUpdate();
                  break;
               }
               else {
                   System.out.println("8자 이상으로 입력하세요");
                   continue;
                }
            }
               System.out.println("회원가입이 되었습니다.");
               break;
         }
      }catch(SQLException e) {
         e.printStackTrace();
      }
   }
   
   public void login() {
      try {
         while(true) {
            System.out.println("아이디: ");
            id = scanner.next();
            System.out.println("비밀번호: ");
            String pw = scanner.next();
            callname = "{call checkpw(?,?,?)}";
            cstmt =con.prepareCall(callname);
            cstmt.setString(1, id);
            cstmt.setString(2, pw);
            cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
            cstmt.executeQuery();
            String result = cstmt.getString(3);
            if(result.equals("로그인 성공")) {
               login = true;
               System.out.println(result);
               break;
            }
            else
               System.out.println(result);
         }
      }catch(SQLException e) {
         e.printStackTrace();
      }
   }
   
   public void doc_info() {
      try {
            System.out.println("확인하고 싶은 의사의 이름을 쓰세요");
              String doc_name=scanner.next();
              String query="SELECT doc_name,doc_officetel, doc_major FROM doctor WHERE doc_name=?";
              pstmt=con.prepareStatement(query);
              pstmt.setString(1,doc_name);
              rs=pstmt.executeQuery();
              
          
              System.out.println("<의사 정보 확인>");
                 
              while(rs.next()) {
                 error = true;
                 doc_name=rs.getString(1);
                 String doc_officetel=rs.getString(2);
                 String doc_major = rs.getString(3);
                 
                 System.out.println("의사 이름:" + doc_name+", 의사 번호: "+doc_officetel+", 의사 전공: "+doc_major);
                 }

        
         if(!error)
            System.out.println("해당 이름의 의사가 존재하지 않습니다");
        }catch(SQLException e) {
           e.printStackTrace();
        }
      error = false;
   }
   
   public void orderlist() {
      try {
           String query="SELECT order_id,prod_name,order_date,amount FROM orders,product WHERE orders.mem_id=? and product.prod_id=orders.prod_id";
           pstmt=con.prepareStatement(query);
           pstmt.setString(1,id);
           rs=pstmt.executeQuery();
           
      
           System.out.println("<구매 내역>");
           while(rs.next()) {
              String order_id=rs.getString(1);
              String prod_name=rs.getString(2);
              Date order_date=rs.getDate(3);
              int amount=rs.getInt(4);
              
              System.out.println("주문ID: "+order_id+", 상품명: "+prod_name+", 주문 일자: "+order_date+", 수량: "+amount);
           }
        }catch(SQLException e) {
           e.printStackTrace();
        }
   }
   
   public void deliver() {
      try {
            System.out.println("확인하고 싶은 배송상태를 입력하세요: (배송중, 배송완료, 배송준비중)");
            String order = scanner.next();
            String query="SELECT p.prod_name, o.amount FROM product p, orders o WHERE mem_id=? and order_state=? and o.prod_id = p.prod_id";
            pstmt=con.prepareStatement(query);
            pstmt.setString(1,id);
            pstmt.setString(2,order);
            rs = pstmt.executeQuery();
            
            while(rs.next()) {
               error=true;
               String prod_name = rs.getString(1);
               int amount =rs.getInt(2);
               System.out.println("상품 이름: "+prod_name+", 수량: "+amount);
            }
            if(!error)
               System.out.println("해당 정보가 존재하지 않습니다.");
        }
        catch(SQLException e) {
           e.printStackTrace();
        }
      }
   
   public void reserve() {
      try {
            //의사출력
           System.out.println("진료를 원하는 의사를 선택하세요");
           String printDoc = "select doc_name, doc_major from doctor";
           stmt = con.createStatement();
           rs = stmt.executeQuery(printDoc);
           System.out.println("의사 이름           전공");
           while(rs.next()) {
               String docName = rs.getString(1);
               String docMajor = rs.getString(2);
               System.out.println(docName + " " +docMajor);
            }
            String rsvDocName = scanner.next();
            //의사 id구하기
            String findDoc = "select doc_id from doctor where doc_name like '"+rsvDocName+"'";
            rs = stmt.executeQuery(findDoc);
            int rsvDocId = 0;
            if(rs.next())
               rsvDocId = rs.getInt(1);
            
            //본격적으로 날짜와 시간입력으로 예약하기
            while(true) {
            System.out.println("원하는 날짜 와 시간을 입력하세요:(yyyymmdd, HH:MM)");
            String rsvDate = scanner.next();
            String rsvTime = scanner.next();
            callname = "{call check_reserve(?,?,?,?,?)}";
            String result = null;
            cstmt = con.prepareCall(callname);
            cstmt.setString(1, id);
            cstmt.setInt(2, rsvDocId);
            cstmt.setString(3, rsvDate);
            cstmt.setString(4, rsvTime);
            cstmt.registerOutParameter(5, java.sql.Types.VARCHAR);
            cstmt.executeQuery();
            result = cstmt.getString(5);
            
            
            if(result.equals("예약이 꽉 찼습니다.") || result.equals("이미 다른 시간에 예약이 되었습니다.")) {
            	System.out.println(result);
            	System.out.println("1.예약 다시 잡기\n2.예약 종료");
            	int choice = scanner.nextInt();
            	if(choice == 1)
            		continue;
            	else {
            		System.out.println("예약을  종료합니다.");
            		return;
            	}
            }
            else {
               System.out.println(result);
               break;
               }
           }
         }catch(SQLException e) {e.printStackTrace();}

   }
   
   public void checkReserve() {
      try {
         System.out.println("<예약 조회>");
         String query = "select r.reserv_date, r.reserv_time, d.doc_name from reservation r, doctor d where r.doc_id = d.doc_id and mem_id =?";
         
         pstmt = con.prepareStatement(query);
         
         pstmt.setString(1, id);
         
         rs = pstmt.executeQuery();
         while(rs.next())
         {
            Date reserv_date = rs.getDate(1);
            String reserv_time = rs.getString(2);
            String doc_name = rs.getString(3);
            
            System.out.println("예약 날짜: "+reserv_date+", 예약 시간: "+reserv_time+", 담당 의사: "+doc_name);
         }
      }catch(SQLException e) {
         e.printStackTrace();
      }
   }
   
   public static void main(String[] args) {
      animal_hospital an = new animal_hospital();
      
         while(!login) {
            System.out.println("=========================");
            System.out.println("1. 회원 가입      ");
            System.out.println("2. 로그인      ");
            System.out.println("=========================");
            
            int num = an.scanner.nextInt();
            switch(num) {
            case 1:
               an.register();
               break;
            case 2:
               an.login();
               break;
            }
         }
         while(true) {
            System.out.println("============================");
            System.out.println("1. 날짜에 따른 진료 내역 확인   ");
            System.out.println("2. 건강검진 내역 확인");
            System.out.println("3. 예약하기");
            System.out.println("4. 예약 조회");
            System.out.println("5. BMI 측정");
            System.out.println("6. 의사 정보확인");        
            System.out.println("7. 배송내역확인");
            System.out.println("8. 구매 내역");
            System.out.println("9. 주문 교환 내역 확인");
            System.out.println("10. 주문 취소 내역 확인");
            System.out.println("11. 종료");
            System.out.println("============================");
            int num = an.scanner.nextInt();
            if(num == 11) {
               System.out.println("프로그램 종료");
               break;
            }
               
            switch(num) {
            case 1:
               an.medical_details();
               continue;
            case 2:
               an.printCheckUp();
               continue;
            case 3:
               an.reserve();
               continue;
            case 4:
               an.checkReserve();
               continue;
            case 5:
               an.bmi();
               continue;
            case 6:
               an.doc_info();
               continue;
            case 7:
               an.deliver();
               continue;
            case 8:
               an.orderlist();
               continue;
            case 9:
               an.changeOrder();
               continue;
            case 10:
               an.cancelOrder();
               continue;
            }
         }
      }
 }