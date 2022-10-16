package s13;

import java.sql.*;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

class MenuPanel extends JFrame implements ActionListener
{
	private JPanel pan1,pan2,pan3,pan4,pan5;
	private JLabel lbl = new JLabel("桌次 : ");
	private JSpinner spin1 = new JSpinner(new SpinnerNumberModel(0,0,10,1));
	private JButton btn = new JButton("確定");
	private JRadioButton[] rdb = new JRadioButton[4];
	private JCheckBox[] chkS = new JCheckBox[2];
	private String[] dessert = {"請選擇","法式布蕾","德式布丁","葡式蛋塔","美式軟餅乾"};
	private JComboBox<String>ch_dessert = new JComboBox<String>(dessert);
	static JTextArea txtOrder = new JTextArea();
	private Connection conn;
	private Statement stmt;
	private String db_name = "OrderSystem";
	private String db_table = "orderList";
	private ResultSet rs;
	
	void ErrMsg(String msg, Exception e)
	{
		JOptionPane.showMessageDialog(null, msg + "\n訊息：" + e, "錯誤訊息", JOptionPane.ERROR_MESSAGE);
	}
	
	void FileClose()
	{
		if (rs != null)
		{
			try
			{
				rs.close();
			}
			catch (SQLException e)
			{
				ErrMsg("關閉資料庫產生的錯誤！", e);
			}
			rs = null;
		}

		if (stmt != null)
		{
			try
			{
				stmt.close();
			}
			catch (SQLException e)
			{
				ErrMsg("關閉資料庫產生的錯誤！", e);
			}
			stmt = null;
		}
	}
	
	void CconfirmDialog() {
		int question = JOptionPane.showConfirmDialog(null, "請再次確認您的餐點是否正確!", "確認訊息",
			JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
	
		if(question == JOptionPane.YES_OPTION)
			JOptionPane.showMessageDialog(null, "已收到您的餐點資訊!\n餐點正在準備中，請稍後片刻", "通知",
					JOptionPane.INFORMATION_MESSAGE);
		else
			JOptionPane.showMessageDialog(null, "請重新選取您的餐點!" , "通知" ,
					JOptionPane.INFORMATION_MESSAGE);
	}
	

	MenuPanel()
	{
		JPanel pan1 = new JPanel();
		pan1.setLayout(null);
		pan1.setBounds(20,30,170,80);
		Border lineA = BorderFactory.createLineBorder(Color.black);
		pan1.setBorder(BorderFactory.createTitledBorder(lineA,"座位資訊 : "));
		add(pan1);
		
		lbl.setBounds(30,30,50,20);
		pan1.add(lbl);
		
		spin1.setBounds(80,30,60,25);
		pan1.add(spin1);
		
		btn.setBounds(260,50,80,30);
		add(btn);
		btn.addActionListener(this);
		
		JPanel pan2 = new JPanel();
		pan2.setLayout(null);
		pan2.setBounds(20,120,345,100);
		pan2.setBorder(BorderFactory.createTitledBorder(lineA,"主餐 : "));
		add(pan2);
		
		rdb[0] = new JRadioButton("鮮烤魚排 (200元)",true);
		rdb[1] = new JRadioButton("牛小排 (300元)");
		rdb[2] = new JRadioButton("焗烤明蝦 (250元)");
		rdb[3] = new JRadioButton("法式烤雞 (260元)");
		ButtonGroup grpRdb = new ButtonGroup();
		for(int i = 0; i < rdb.length ; i++)
		{
			grpRdb.add(rdb[i]);
			pan2.add(rdb[i]);
		}
		rdb[0] .setBounds(30,20,120,30);
		rdb[1] .setBounds(180,20,120,30);
		rdb[2] .setBounds(30,50,120,30);
		rdb[3] .setBounds(180,50,120,30);
		
		JPanel pan3 = new JPanel();
		pan3.setBounds(20,240,170,100);
		pan3.setBorder(BorderFactory.createTitledBorder(lineA,"副餐 : "));
		add(pan3);
		
		chkS[0] = new JCheckBox("鮮蝦蘆筍 (80元)");
		chkS[1] = new JCheckBox("香炸豆腐 (50元)");
		for(int j = 0; j < chkS.length ; j++)
		{
			pan3.add(chkS[j]);
		}
		
		JPanel pan4 = new JPanel();
		pan4.setBounds(230,240,135,70);
		pan4.setBorder(BorderFactory.createTitledBorder(lineA,"甜點 (免費) "));
		add(pan4);
		pan4.add(ch_dessert);
		
		JPanel pan5 = new JPanel();
		pan5.setLayout(null);
		pan5.setBounds(20,350,345,150);
		pan5.setBorder(BorderFactory.createTitledBorder(lineA,"點餐資訊 : "));
		pan5.setBackground(Color.white);
		add(pan5);
		
		txtOrder.setBounds(20,30,300,110);
		pan5.add(txtOrder);
		
		setTitle("點餐表單");
		setLayout(null);
		setBounds(100,100,400,550);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		repaint();
	}
	
	public void actionPerformed(ActionEvent ex)
	{
		int[] rdb_P = {200,300,250,260};
		int[] chkS_P = {80,50};
		int total = 0;
		String main_dish = null;
		String []side_dish = new String[chkS.length+1];
		String dessert_choose = null;
		
		txtOrder.setText("歡迎使用點餐系統 !\n");
		
		if(ex.getSource() == btn)
		{
			for(int i = 0; i < rdb.length;i++)
				if(rdb[i].isSelected())
				{
					txtOrder.append("  桌次 : "+spin1.getValue()+"桌\n");
					
					if(spin1.getValue().toString().equals("0"))
					{
						ErrMsg("錯誤!桌次未選擇！" , new Exception());
						return;
					}
					
					txtOrder.append("  主餐 : "+rdb[i].getText()+"\n");
					main_dish = rdb[i].getText();
					total += rdb_P[i];
				}
			
			txtOrder.append("  副餐 : ");
			for(int i = 0; i < chkS.length ; i++)
			{
				if(chkS[i].isSelected())
				{
					txtOrder.append(chkS[i].getText()+" ");
					total += chkS_P[i];
					side_dish[i] = chkS[i].getText();
				}
			}
			if(chkS[0].isSelected()==false && chkS[1].isSelected()==false)
				txtOrder.append("未選擇");
				
			
			side_dish[chkS.length] = null;
			
			if(dessert[ch_dessert.getSelectedIndex()].equals("請選擇"))
				txtOrder.append("\n  甜點 : 未選擇");
				
			else
				{
				txtOrder.append("\n  甜點 : "+dessert[ch_dessert.getSelectedIndex()]+"( 免費 )");
				dessert_choose = dessert[ch_dessert.getSelectedIndex()];
				}
			txtOrder.append("\n  合計 : "+total+"元");
		}
		CconfirmDialog();
		
		try
		{
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"
					+ db_name + "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
					"Felix","123456789");
			stmt = conn.createStatement();
			stmt.execute("INSERT INTO "+ db_table 
					+ " (No, table_number, mainDish, sideDish1, sideDish2, sideDish3, dessert, price) VALUES (NULL,'"
					+ spin1.getValue() + "','" + main_dish + "','" + side_dish[0] + "','"
					+ side_dish[1] + "','" + side_dish[2] + "','" + dessert_choose + "','"
					+ total + "')");
		
		}
		catch(SQLException e)
		{
			System.out.println(e);
			System.out.println("新增資料錄時，發生錯誤!");
		}
		FileClose();
		
		txtOrder.setText("歡迎使用點餐系統 !\n\n如要繼續點餐，請直接點選 !");
	}
}

class CCreateOrderdbX
{
	private Connection conn;
	private Statement stmt;
	private String db_name = "OrderSystem";
	private String db_table = "orderList";

	CCreateOrderdbX(){
	try
	{
		Class.forName("com.mysql.cj.jdbc.Driver");
	}
	catch (Exception e)
	{
		ErrMsg("MySQL驅動程式安裝失敗！", e);
	}

	try
	{
		conn = DriverManager.getConnection(
			"jdbc:mysql://localhost:3306/?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "Felix",
			"123456789");
		stmt = conn.createStatement();
		String Create_DB = "CREATE DATABASE IF NOT EXISTS " + db_name + " CHARACTER SET utf8 COLLATE utf8_unicode_ci";
		stmt.executeUpdate(Create_DB);
	}
	catch (SQLException e)
	{
		if (stmt != null)
			ErrMsg("建立" + db_name + "資料庫時，發生錯誤！", e);
		else
			ErrMsg("MySQL無法啟動！", e);
	}
	catch (Exception e)
	{
		ErrMsg("發生其他錯誤！", e);
	}

	try
	{
		conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + db_name
				+ "?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC", "Felix", "123456789");
		stmt = conn.createStatement();

		String Create_TB = "CREATE TABLE IF NOT EXISTS " + db_table;
		Create_TB += " (No INT NOT NULL AUTO_INCREMENT COMMENT '點餐序號',";
		Create_TB += " table_number INT(3) NOT NULL COMMENT '桌號', ";
		Create_TB += "mainDish VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci "
				+" NOT NULL COMMENT '主餐',";
		Create_TB += "sideDish1 VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci "
				+" COMMENT '副餐A',";
		Create_TB += "sideDish2 VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci "
				+" COMMENT '副餐B',";
		Create_TB += "sideDish3 VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci "
				+" COMMENT '副餐C',";
		Create_TB += "dessert VARCHAR(30) CHARACTER SET utf8 COLLATE utf8_unicode_ci "
				+" COMMENT '甜點',";
		Create_TB += "price INT NOT NULL COMMENT '價格',";
		Create_TB += " PS VARCHAR(20) CHARACTER SET utf8 COLLATE utf8_unicode_ci "
				+ " COMMENT '備註',";
		Create_TB += " stamp TIMESTAMP on update CURRENT_TIMESTAMP NOT NULL"
				+" DEFAULT CURRENT_TIMESTAMP COMMENT '更新時間',";
		Create_TB += " PRIMARY KEY (No)) ENGINE = InnoDB";
		stmt.executeUpdate(Create_TB);

		//JOptionPane.showMessageDialog(null, "點餐資料表建立成功！");
		stmt.close();
		conn.close();
	}
	catch (SQLException e)
	{
		if (stmt != null)
			ErrMsg("建立" + db_table + "資料表時，發生錯誤！", e);
		else
			ErrMsg("MySQL無法啟動！", e);
	}
	catch (Exception e)
	{
		ErrMsg("發生其他錯誤！", e);
	}
	}
	void ErrMsg(String msg, Exception e)
	{
		JOptionPane.showMessageDialog(null, msg + "\n訊息：" + e, "錯誤訊息", JOptionPane.ERROR_MESSAGE);
	}
}
	
public class s13 {
	public static void main(String[] args) {
		new CCreateOrderdbX();
		new MenuPanel();
	}
}
