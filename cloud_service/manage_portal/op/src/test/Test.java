package test;

import java.util.HashMap;
import java.util.Map;

import com.zhicloud.op.service.constant.MonitorConstant;

import net.sf.json.JSONObject;





public class Test
{
	private static Map<String,String> testMap = new HashMap<String,String>();
	public static void main(String[] args) throws Exception
	{
		/*System.out.println(new BigInteger("1024").pow(5));
		String message = new String("【致云科技】尊敬的DavidYang您好，您的余额还可以支付3天的费用，为不影响您云主机的正常使用，请及时充值。谢谢"); 
		String state  =new SendSms().zhicloudSendSms("18628094966",message);
		if (state.equals("1")) {
			System.out.println("成功");
		} 
		Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
		terminalUserData.put("email", "yangwei@zhicloud.com");
		terminalUserData.put("account", "DavidYang");
		terminalUserData.put("day", 3);
		new SendMail().sendRechargeMessaToTerminalUser(terminalUserData);
		System.out.println("成功");*/
//		Calendar c = Calendar.getInstance();
//		c.add(Calendar.DAY_OF_MONTH, 7);
//		System.out.println(c);
//		Date d = StringUtil.stringToDate(str)
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		Date d = StringUtil.stringToDate("20140928131012825","yyyyMMddHHmmssSSS");
//		c.setTime(d);
//		c.add(Calendar.DAY_OF_MONTH,7);
//		System.out.println(sdf.format(c.getTime()));
//		final Timer timer = new Timer();  
//		TimerTask tt = new TimerTask() {
//			int n = 0;
//			@Override
//			public void run() {
//				n++;
//				System.out.println(n);
//				if (n==5) {
//					timer.cancel();
//				}
//			}
//		}; 
//       timer.schedule(tt,0,3000);
		
//		String pwsString2="f90b656c1fb8929e3786f6c44098e8f4f141bdebd3f70a10";
//		System.out.println(pwsString2.length());
//		System.out.println(pwsString2);
//		System.out.println("f90b66c1fb929e386f6c4098ef4f14bdebdf70a1");
//		StringBuilder pwStringBuilder= new StringBuilder();
//		for (int i = 0; i < pwsString2.length()-1; i=i+6) 
//		{
//			pwStringBuilder.append(pwsString2.substring(i, i+5));
//		}
//		
//		pwStringBuilder.toString();
//	   System.out.println(pwStringBuilder);
		
//		System.out.println(new RandomPassword().getRandomPwd(16)); 
//		System.out.println(new RandomPassword().getRandomNum(6));
//		String  eCheck = "^([a-zA-Z0-9]?[-_.|\\/]?)+([a-zA-Z0-9]?[-_.|\\/]+)*@([a-zA-Z0-9]*.?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
//		String  pCheck ="^1[3|4|5|8][0-9]\\d{8,8}$";
//		Pattern  ePattern = Pattern.compile(eCheck);    
//		Pattern  pPattern  = Pattern.compile(pCheck);
//		System.out.println(ePattern.matcher("t_.@live.cn").matches());
//		System.out.println(pPattern.matcher("18688886666").matches());
//		
//		BigDecimal balance = new BigDecimal(1);
//		System.out.println(balance.compareTo(new BigDecimal(0)));
//		
//		System.out.println(StringUtil.generateRandom(20));
		
//			Calendar c = Calendar.getInstance();
//			c.add(Calendar.YEAR, -1);
//			System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(c.getTime()));
//		
//		  Calendar b = Calendar.getInstance();
//		  b.add(Calendar.DAY_OF_YEAR, -7);
//		  System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(b.getTime()));
//		  
//		  Calendar a = Calendar.getInstance();
//		  a.add(Calendar.MONTH, -1);
//		  System.out.println(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(a.getTime()));
		
//		Calendar a = Calendar.getInstance();
//		Calendar b = Calendar.getInstance();
//		Calendar c = Calendar.getInstance();
//		a.add(Calendar.YEAR, -1);
//		b.add(Calendar.DAY_OF_YEAR,-30);
//		c.add(Calendar.MONTH,-12);
//		String yearAgo  =new SimpleDateFormat("yyyyMMddHHmmssSSS").format(a.getTime());
//		String weekAgo  =new SimpleDateFormat("yyyyMMddHHmmssSSS").format(b.getTime());
//		for (int i = 0; i < 30; i++){
//			b.add(Calendar.DAY_OF_YEAR,+1);
//			String monthAgo =new SimpleDateFormat("yyyyMMddHHmmssSSS").format(b.getTime());
//			System.out.println(monthAgo);
//		}
		
//		System.out.println(yearAgo+"-----"+weekAgo+"----"+monthAgo);
		
//		String time  =new SimpleDateFormat("yyyyMMdd").format(b.getTime());
//		
//		int [] user ={};
//		System.out.println(user.length);
//		System.out.println((0%3));
//		System.out.println("just test git branch");
//		testSet.add("1");
//		testSet.add("2");
//		testSet.add("3");
//		testSet.add("4");
//		for(String s : testSet){
//			System.out.println(s);
//		}
//		testMap.put("123456","123456");
//		if(testMap.containsValue("123456")){
//			testMap.put("123456","654321");
//		}
//		System.out.println(testMap.get("123456"));
//		String s = "abc\0efg\0hj\0";
//		String[] ss = s.split("\0");
//		System.out.println(ss[0]);
	}
	
}










