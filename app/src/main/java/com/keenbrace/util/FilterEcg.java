package com.keenbrace.util;



public class FilterEcg {
	static final short LOWPASS_ORDER = 128;
	static short[][] lowpas_dat_buf = new short[12][LOWPASS_ORDER];
	static short filter_baseline_buf[][] = new short[12][35];
	static final short DELAY = 70;

	private static short filter_lowpass05(short px, short ch) {
		int sum = 0;
		for (int i = 0; i < (LOWPASS_ORDER - 1); i++) {
			lowpas_dat_buf[ch][i] = lowpas_dat_buf[ch][i + 1];
			sum += lowpas_dat_buf[ch][i];
		}
		lowpas_dat_buf[ch][(LOWPASS_ORDER - 1)] = px;
		sum += px;
		px = (short) (sum / LOWPASS_ORDER);
		return px;
	}

	static short[][] Delay = new short[8][DELAY];
	static short[] pwrite = new short[8];
	static short[] pread = new short[8];

	static float[][] m1 = new float[12][1];
	static float[][] m2 = new float[12][1];
	static float[][] num = { { 0.9754783907505f, -0.603069256509f,
			0.9754783907505f } };
	static float[][] den = { { 1, -0.603069256509f, 0.9509567815011f } };
	static float gain = 0.9754783907505f;

	static float[] num_35 = { 0.1173510367246f, 0.2347020734492f,
			0.1173510367246f };
	static float[] den_35 = { 1, -0.8252323806895f, 0.2946365275879f };
	static float gain_35 = 1;// 0.1173510367246;
	static float m1_35[] = new float[12];
	static float m2_35[] = new float[12];

	public static short filter_all(short px, short ch) {
		px = filter_baseline(px, (short) 0);
		px = filter_bandstop50(px, (short) 0);
		px = filter_lowpass_35Hz(px, (short) 0);
		return px;

	}

	// �˻���
	public static short filter_baseline(short px, short ch) {
		short tmp = 0;
		short j = 0;
		tmp = px;
		tmp = filter_lowpass05(tmp, ch);
		// -------------------------------------------
		// if(b_ini==0)
		// {
		// for(j=0;j<DELAY;j++)
		// {
		// Delay[ch][j]=px[0];
		// }
		// }
		// if(++pwrite[ch] >= DELAY) pwrite[ch]=0;
		// Delay[ch][pwrite[ch]]=*px;

		if (++pwrite[ch] >= DELAY)
			pwrite[ch] = 0;
		Delay[ch][pwrite[ch]] = px;

		pread[ch] = (short) (pwrite[ch] + 1);
		if (pread[ch] >= DELAY)
			pread[ch] = 0;

		px = (short) (Delay[ch][pread[ch]] - tmp);// ԭʼ���ݼ�ȥ��ͨ����=��ͨ����
		return px;
	}

	// 50HZ
	public static short filter_bandstop50(short px, short ch) {
		float x; /* gain * input sample */
		float temp; /* scratch variable */
		float s = 0; /* scratch variable */
		x = px * gain;
		for (short j = 0; j < 1; j++) {
			temp = (den[j][0] * x - den[j][1] * m1[ch][j] - den[j][2]
					* m2[ch][j])
					/ den[j][0];
			s = (num[j][0] * temp + num[j][1] * m1[ch][j] + num[j][2]
					* m2[ch][j])
					/ den[j][0];
			m2[ch][j] = m1[ch][j];
			m1[ch][j] = temp;
			x = s;
		}
		px = (short) s;
		return px;
	}

	// ŷ���г����õ�
	public void filter_bandstop60(short[] px, short ch) {

		short NUMOFSECTION = 1;
		double[][] num = { { 0.9754783907505, -0.1225402894263, 0.9754783907505 } };

		double den[][] = { { 1, -0.1225402894263, 0.9509567815011 } };

		double gain = 0.9754783907505;

		double[][] m1 = new double[12][1];
		double[][] m2 = new double[12][1];

		double x; /* gain * input sample */
		double temp; /* scratch variable */
		double s = 0; /* scratch variable */

		x = px[0] * gain;

		for (short j = 0; j < 1; j++) {
			temp = (den[j][0] * x - den[j][1] * m1[ch][j] - den[j][2]
					* m2[ch][j])
					/ den[j][0];
			s = (num[j][0] * temp + num[j][1] * m1[ch][j] + num[j][2]
					* m2[ch][j])
					/ den[j][0];
			m2[ch][j] = m1[ch][j];
			m1[ch][j] = temp;
			x = s;
		}

		px[0] = (short) s;

	}

	// 35HZ�˲�
	public static short filter_lowpass_35Hz(short px, short ch) {
		float x; /* gain * input sample */
		float temp; /* scratch variable */
		float s; /* scratch variable */
		x = px * gain_35;
		temp = (den_35[0] * x - den_35[1] * m1_35[ch] - den_35[2] * m2_35[ch])
				/ den_35[0];
		px = (short) ((num_35[0] * temp + num_35[1] * m1_35[ch] + num_35[2]
				* m2_35[ch]) / den_35[0]);
		m2_35[ch] = m1_35[ch];
		m1_35[ch] = temp;
		return px;
	}

}
