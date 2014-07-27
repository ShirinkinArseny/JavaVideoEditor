package JVE.Commands.Effects;

import JVE.Commands.Command;

import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;

public class ApplyRemix extends Command {

    private String[] args;

    public ApplyRemix(String[] s) {
        args = s;
    }

    @Override
    public BufferedImage doAction(BufferedImage canva) throws Exception {
        float[] matrix=new float[16];
        for (int i=0; i<16; i++)
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

                int nR = (int) (matrix[0]*R+matrix[1]*G+matrix[2]*B+matrix[3]*A);
                int nG = (int) (matrix[4]*R+matrix[5]*G+matrix[6]*B+matrix[7]*A);
                int nB = (int) (matrix[8]*R+matrix[8]*G+matrix[10]*B+matrix[11]*A);
                int nA = (int) (matrix[12]*R+matrix[13]*G+matrix[14]*B+matrix[15]*A);

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
