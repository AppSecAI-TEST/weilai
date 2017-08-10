/**
 * Project Name:enterpriseuniversity 
 * File Name:ExamTest.java 
 * Package Name:com.chinamobo.ue.api.exam.service
 * @author Acemon
 * Date:2017年7月31日上午11:45:24
 */
package com.chinamobo.ue.api.exam.service;

/**
 * ClassName:ExamTest
 * Function:TODO
 * @author Acemon
 * 2017年7月31日
 */
public class ExamTest {

	/**
	 * 功能描述：[]
	 * 创建者：Acemon
	 * 创建时间：2017年7月31日
	 */
	public static void main(String[] args) {
		String[][] users = new String[][]{
			{"M00008","459cc49c-144f-4472-826c-8a33dcea07d2"},
			{"M00055","459cc49c-144f-4472-826c-8a33dcea07d3"},
			{"M00030","459cc49c-144f-4472-826c-8a33dcea07d4"},
			{"M00007","459cc49c-144f-4472-826c-8a33dcea07d5"},
			{"M00051","459cc49c-144f-4472-826c-8a33dcea07d7"},
			{"mayanqing","459cc49c-144f-4472-826c-8a33dcea07d8"},
			{"yinhao","459cc49c-144f-4472-826c-8a33dcea07d9"},
			{"M00032","459cc49c-144f-4472-826c-8a33dcea01d7"},
			{"lixuinxuan","459cc49c-144f-4472-826c-8a33dcea02d7"},
			{"mynextev","459cc49c-144f-4472-826c-8a33dcea03d7"},
			{"M00012","459cc49c-144f-4472-826c-8a33dcea04d7"},
			{"M00050","459cc49c-144f-4472-826c-8a33dcea05d0"},
			{"M00003","459cc49c-144f-4472-826c-8a33dcea06d7"},
			{"M00001","459cc49c-144f-4472-826c-8a33dcea08d8"},
			{"gaojiaqi","459cc49c-144f-4472-826c-8a33dcea09d7"},
			{"M000A1","459cc49c-144f-4472-826c-8a33dcea27d7"},
			{"M00004","459cc49c-144f-4472-826c-8a33dcea37d7"},
			{"M00006","459cc49c-144f-4472-826c-8a33dcea57d7"},
			{"M00021","459cc49c-144f-4472-826c-8a33dcea67d7"},
			{"M00020","459cc49c-144f-4472-826c-8a33dcea77d7"},
			{"M00022","459cc49c-144f-4472-826c-8a33dcla87d8"},
			{"M00013","459cc49c-144f-4472-826c-8a33dcea97d7"},
			{"M00015","459cc49c-144f-4472-826c-8a33dcea17d9"},
			{"M00009","459cc49c-144f-4472-826c-8a33dcea47d7"},
			{"wuxiaoxue","459cc49c-144f-4472-826c-8a33dcea10d7"},
			};
			for(int i= 0 ;i<25;i++){
				MyThread mt = new MyThread(users[i][0],users[i][1]);
				Thread t1 = new Thread(mt);
				t1.start();
			}
		
	}

}
