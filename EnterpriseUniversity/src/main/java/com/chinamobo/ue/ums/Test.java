package com.chinamobo.ue.ums;




import com.chinamobo.ue.utils.MessagePost;


public class Test {

	public static void main(String[] args) throws Exception {
//		String mobile ="13756137942,15210337132,";
		String str = "ewdfs中国";
		try{
		/*String res=MessagePost.putMessage(mobile, msg);
		System.out.println(res);*/
			
		//	System.out.println(getWordCount(str));
			Charcount("ABAACGDHGSTHJHH");

		}catch(Exception e){
			e.printStackTrace();
		}
	}
	//判断一个字符的字节长度
	public  static int  getWordCount(String s){
         int  length  =   0 ;
         for ( int  i  =   0 ; i  <  s.length(); i ++ ){
             int  ascii  =  Character.codePointAt(s, i);
             if (ascii  >=   0   &&  ascii  <= 255 )
                length ++ ;
             else 
                length  +=   2 ;
                
        }
         return  length;
	        
	}
	 public static void Charcount(String string) {
	        if (string == null)
	            return;
	        int[] count = new int[string.length()];

	        for (int i = 0; i < count.length; i++) {
	            // 将字符串中索引字符存在临时变量中
	            char mid = string.charAt(i);
	            for (int j = 0; j < count.length; j++) {
	                if (mid == string.charAt(j))
	                    count[i]++;
	            }
	        }
	        // 得到次数最多的字符
	        int index = 0;
	        int max = count[0];
	        for (int i = 0; i < count.length; i++) {
	            if (max < count[i]) {
	                max = count[i];
	                index = i;
	            }
	        }
	        System.out.println();
	        System.out.println("字符=" + string.charAt(index));
	        System.out.println("次数=" + count[index]);
	    }

	
}
