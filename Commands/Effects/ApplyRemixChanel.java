package JVE.Commands.Effects;

import JVE.Commands.Command;

import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;

public class ApplyRemixChanel extends Command {

    private String[] args=new String[4];
    private boolean[] chanel=new boolean[]{false, false, false, false};

    public ApplyRemixChanel(String[] s) {
         System.arraycopy(s, 0, args, 0, 4);
        for (int i=4; i<s.length; i++)
        switch (s[i].toLowerCase()) {
            case "red": chanel[0]=true; break;
            case "green": chanel[1]=true; break;
            case "blue": chanel[2]=true; break;
            case "alpha": chanel[3]=true; break;
        }
    }

    @Override
    public BufferedImage doAction(BufferedImage canva) throws Exception {
        float[] matrix=new float[4];
        for (int i=0; i<4; i++)
            matrix[i]=parseTimedFloat(args[i]);
        RGBImageFilter mixer = new RGBImageFilter()
        {
            @Override
            public int filterRGB(int x, int y, int nRGB)
            {
                int A = (nRGB >> 24) & 0xff;
                int R = (nRGB >> 16)  & 0xff;
                int G = (nRGB >> 8)   & 0xff;
                int B = nRGB & 0xff;

                int nR = chanel[0]?(int) (matrix[0]*R+matrix[1]*G+matrix[2]*B+matrix[3]*A):R;
                int nG = chanel[1]?(int) (matrix[0]*R+matrix[1]*G+matrix[2]*B+matrix[3]*A):G;
                int nB = chanel[2]?(int) (matrix[0]*R+matrix[1]*G+matrix[2]*B+matrix[3]*A):B;
                int nA = chanel[3]?(int) (matrix[0]*R+matrix[1]*G+matrix[2]*B+matrix[3]*A):A;

                nR=Math.max(0, Math.min(255, nR));
                nG=Math.max(0, Math.min(255, nG));
                nB=Math.max(0, Math.min(255, nB));
                nA=Math.max(0, Math.min(255, nA));

                return ((nA << 24) | (nR << 16) |
                        (nG << 8)  | (nB));

            }
        };
        return applyRGBFilter(canva, mixer);
    }

}
