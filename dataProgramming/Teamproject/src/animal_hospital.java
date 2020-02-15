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
         //JDBC ����̹� �Ί�
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
               System.out.println("�ݷ������� ǰ���� �Է��ϼ���:");
              String p_animal_species=scanner.next();
               System.out.println("�ݷ������� ü���� �Է��ϼ���:");
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
              
              System.out.println("��� ü�߰��� ������"+p_deviation+" �Դϴ�." );
              System.out.println("�ݷ������� "+result);
            }
            
            catch(SQLException e) {
               e.printStackTrace();
            }
      }
   
   public void printCheckUp() {
	      try{
	         int aId=0;
	         System.out.println("�ǰ��������� Ȯ���ϰ���� ���� �̸��� �Է��Ͻÿ�: ");
	      aName = scanner.next();
	      System.out.println("<�ǰ���������>");
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

	         System.out.println( "����: "+height+", ������: "+weight+", ���װ˻�: "+bloodTest+", �������� �˻�: "+x_ray+", ���� ��¥: "+chkupDate);
	         
	      }
	      if(!error) {
	         System.out.println("��ȸ�� �����Ͱ� �����ϴ�.");
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
                  + "order_state like '��ȯ' order by order_date";
            stmt = con.createStatement();
            rs = stmt.executeQuery(changeOrder);
            System.out.println("<�ֹ� ��ȯ ����>");
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
               System.out.println("��ǰ��: "+prodName+", �ֹ� ����: "+Integer.toString(amount)+"�ֹ� ����: "+orderState+ ", �ֹ� ��¥: "+day);
               
            }

         if(!error)
            System.out.println("��ȸ�� �����Ͱ� �����ϴ�.");
         
         error = false;
      }catch(SQLException e) {e.printStackTrace();}
   }
   
   public void cancelOrder() {
      try {
            Connection con2 = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
            ResultSet rs2 = null;
            Statement stmt2 = con.createStatement();
            System.out.println("\n"+id+"�� �ֹ� ��ҳ����� Ȯ���մϴ�.\n");
            System.out.println("<�ֹ� ��ȯ ����>");
            String cancelOrder = "select order_date,amount,order_state,prod_id from orders where mem_id like '"+id+"' and "
                  + "order_state like '���' order by order_date";
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
               System.out.println("��ǰ��: "+prodName+", �ֹ� ����: "+Integer.toString(amount)+"�ֹ� ����: "+orderState+ ", �ֹ� ��¥: "+day);
               }

         if(!error)
            System.out.println("��ȸ�� �����Ͱ� �����ϴ�.");
         error = false;
      }catch(SQLException e) {e.printStackTrace();}
   }
   //���� üũ
   public void medical_details() {
      try {
    	  while(true) {
      //��¥�� ���� ���� ���� Ȯ�κ��� �ٽ�
      System.out.println("���� ������ �ñ��� ������ �̸��� �Է��ϼ���: ");
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
      
      if(result.equals("�ش� �ֿ� ������ ��ϵǾ� ���� �ʽ��ϴ�.")) {
    	  System.out.println("�ش� �ֿ� ������ ��ϵǾ� ���� �ʽ��ϴ�.");
    	  continue;
      } 
     
      while(result.equals("���̵� ã�� ����")) {
         System.out.println("Ȯ���� ���ϴ� ��¥�� �Է��ϼ���:(���� q)");
         String date = scanner.next();
         if(date.equals("q"))
            break;
         String query ="select distinct t.sickness_name, t.sickness_symptom, d.doc_name, p.medicine_name, p.medicine_effect from animal a, treatment t, doctor d, cure c, (select p.prescript_id, m.medicine_name, m.medicine_effect from medicine m, prescription p where m.prescript_id = p.prescript_id) p where c.treatment_id = t.treatment_id and d.doc_id = c.doc_id and p.prescript_id = c.prescript_id and a.animal_id = ? and c.cure_visit =?";
         pstmt = con.prepareStatement(query);
         
         pstmt.setInt(1, animalId);
         pstmt.setString(2, date);
         
         rs = pstmt.executeQuery();
         System.out.println("<��¥�� ���� ���᳻�� Ȯ��>");
         while(rs.next()) {
            String sickness_name = rs.getString(1);
            String sickness_symptom = rs.getString(2);
            String doc_name =rs.getString(3);
            String medicine_name = rs.getString(4);
            String medicine_effect = rs.getString(5);
            
            System.out.println("����: "+sickness_name+", ����: "+sickness_symptom+", ���� �ǻ� �̸�: "+doc_name+", �� �̸�:"+medicine_name+", �� ȿ��: "+medicine_effect);
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
        	 System.out.println("���� ������ �����ϴ�.");
         else
        	 System.out.println("�� �ݾ�:" +total);
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
            System.out.println("���̵�: ");
            id = scanner.next();
            callname = "{call CheckDuplicateId(?,?)}";
            cstmt = con.prepareCall(callname);
            cstmt.setString(1, id);
            cstmt.registerOutParameter(2, java.sql.Types.VARCHAR);
            cstmt.executeQuery();
            String result = cstmt.getString(2);
            while(result.equals("��밡���� ���̵��Դϴ�.")) {
               System.out.print("��й�ȣ: ");
               String pw = scanner.next();
               if(pw.length() >= 8)
               {
                  System.out.print("�̸�:");
                  String name = scanner.next();
                  System.out.print("�ּ�:");
                  String addr = scanner.next();
                  System.out.print("����ó:");
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
                   System.out.println("8�� �̻����� �Է��ϼ���");
                   continue;
                }
            }
               System.out.println("ȸ�������� �Ǿ����ϴ�.");
               break;
         }
      }catch(SQLException e) {
         e.printStackTrace();
      }
   }
   
   public void login() {
      try {
         while(true) {
            System.out.println("���̵�: ");
            id = scanner.next();
            System.out.println("��й�ȣ: ");
            String pw = scanner.next();
            callname = "{call checkpw(?,?,?)}";
            cstmt =con.prepareCall(callname);
            cstmt.setString(1, id);
            cstmt.setString(2, pw);
            cstmt.registerOutParameter(3, java.sql.Types.VARCHAR);
            cstmt.executeQuery();
            String result = cstmt.getString(3);
            if(result.equals("�α��� ����")) {
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
            System.out.println("Ȯ���ϰ� ���� �ǻ��� �̸��� ������");
              String doc_name=scanner.next();
              String query="SELECT doc_name,doc_officetel, doc_major FROM doctor WHERE doc_name=?";
              pstmt=con.prepareStatement(query);
              pstmt.setString(1,doc_name);
              rs=pstmt.executeQuery();
              
          
              System.out.println("<�ǻ� ���� Ȯ��>");
                 
              while(rs.next()) {
                 error = true;
                 doc_name=rs.getString(1);
                 String doc_officetel=rs.getString(2);
                 String doc_major = rs.getString(3);
                 
                 System.out.println("�ǻ� �̸�:" + doc_name+", �ǻ� ��ȣ: "+doc_officetel+", �ǻ� ����: "+doc_major);
                 }

        
         if(!error)
            System.out.println("�ش� �̸��� �ǻ簡 �������� �ʽ��ϴ�");
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
           
      
           System.out.println("<���� ����>");
           while(rs.next()) {
              String order_id=rs.getString(1);
              String prod_name=rs.getString(2);
              Date order_date=rs.getDate(3);
              int amount=rs.getInt(4);
              
              System.out.println("�ֹ�ID: "+order_id+", ��ǰ��: "+prod_name+", �ֹ� ����: "+order_date+", ����: "+amount);
           }
        }catch(SQLException e) {
           e.printStackTrace();
        }
   }
   
   public void deliver() {
      try {
            System.out.println("Ȯ���ϰ� ���� ��ۻ��¸� �Է��ϼ���: (�����, ��ۿϷ�, ����غ���)");
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
               System.out.println("��ǰ �̸�: "+prod_name+", ����: "+amount);
            }
            if(!error)
               System.out.println("�ش� ������ �������� �ʽ��ϴ�.");
        }
        catch(SQLException e) {
           e.printStackTrace();
        }
      }
   
   public void reserve() {
      try {
            //�ǻ����
           System.out.println("���Ḧ ���ϴ� �ǻ縦 �����ϼ���");
           String printDoc = "select doc_name, doc_major from doctor";
           stmt = con.createStatement();
           rs = stmt.executeQuery(printDoc);
           System.out.println("�ǻ� �̸�           ����");
           while(rs.next()) {
               String docName = rs.getString(1);
               String docMajor = rs.getString(2);
               System.out.println(docName + " " +docMajor);
            }
            String rsvDocName = scanner.next();
            //�ǻ� id���ϱ�
            String findDoc = "select doc_id from doctor where doc_name like '"+rsvDocName+"'";
            rs = stmt.executeQuery(findDoc);
            int rsvDocId = 0;
            if(rs.next())
               rsvDocId = rs.getInt(1);
            
            //���������� ��¥�� �ð��Է����� �����ϱ�
            while(true) {
            System.out.println("���ϴ� ��¥ �� �ð��� �Է��ϼ���:(yyyymmdd, HH:MM)");
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
            
            
            if(result.equals("������ �� á���ϴ�.") || result.equals("�̹� �ٸ� �ð��� ������ �Ǿ����ϴ�.")) {
            	System.out.println(result);
            	System.out.println("1.���� �ٽ� ���\n2.���� ����");
            	int choice = scanner.nextInt();
            	if(choice == 1)
            		continue;
            	else {
            		System.out.println("������  �����մϴ�.");
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
         System.out.println("<���� ��ȸ>");
         String query = "select r.reserv_date, r.reserv_time, d.doc_name from reservation r, doctor d where r.doc_id = d.doc_id and mem_id =?";
         
         pstmt = con.prepareStatement(query);
         
         pstmt.setString(1, id);
         
         rs = pstmt.executeQuery();
         while(rs.next())
         {
            Date reserv_date = rs.getDate(1);
            String reserv_time = rs.getString(2);
            String doc_name = rs.getString(3);
            
            System.out.println("���� ��¥: "+reserv_date+", ���� �ð�: "+reserv_time+", ��� �ǻ�: "+doc_name);
         }
      }catch(SQLException e) {
         e.printStackTrace();
      }
   }
   
   public static void main(String[] args) {
      animal_hospital an = new animal_hospital();
      
         while(!login) {
            System.out.println("=========================");
            System.out.println("1. ȸ�� ����      ");
            System.out.println("2. �α���      ");
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
            System.out.println("1. ��¥�� ���� ���� ���� Ȯ��   ");
            System.out.println("2. �ǰ����� ���� Ȯ��");
            System.out.println("3. �����ϱ�");
            System.out.println("4. ���� ��ȸ");
            System.out.println("5. BMI ����");
            System.out.println("6. �ǻ� ����Ȯ��");        
            System.out.println("7. ��۳���Ȯ��");
            System.out.println("8. ���� ����");
            System.out.println("9. �ֹ� ��ȯ ���� Ȯ��");
            System.out.println("10. �ֹ� ��� ���� Ȯ��");
            System.out.println("11. ����");
            System.out.println("============================");
            int num = an.scanner.nextInt();
            if(num == 11) {
               System.out.println("���α׷� ����");
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