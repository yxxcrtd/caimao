package com.fmall.bana.utils.captcha;

import com.octo.captcha.component.image.backgroundgenerator.UniColorBackgroundGenerator;
import com.octo.captcha.component.image.color.RandomListColorGenerator;
import com.octo.captcha.component.image.deformation.ImageDeformationByFilters;
import com.octo.captcha.component.image.fontgenerator.RandomFontGenerator;
import com.octo.captcha.component.image.textpaster.DecoratedRandomTextPaster;
import com.octo.captcha.component.image.textpaster.textdecorator.TextDecorator;
import com.octo.captcha.component.image.wordtoimage.DeformedComposedWordToImage;
import com.octo.captcha.component.word.wordgenerator.RandomWordGenerator;
import com.octo.captcha.engine.image.ListImageCaptchaEngine;
import com.octo.captcha.image.gimpy.GimpyFactory;

import java.awt.*;
import java.awt.image.ImageFilter;

/**
 * 验证码
 * Created by Administrator on 2015/12/10.
 */
public class MyCaptchaEngine extends ListImageCaptchaEngine {
    public MyCaptchaEngine() {
    }

    protected void buildInitialFactories() {
        byte minWordLength = 4;
        byte maxWordLength = 4;
        byte fontSize = 25;
        byte imageWidth = 120;
        byte imageHeight = 40;
        RandomWordGenerator wordGenerator = new RandomWordGenerator("23456789abcdefghjkmnpqrstuvwxyz");
        DecoratedRandomTextPaster randomPaster = new DecoratedRandomTextPaster(Integer.valueOf(minWordLength), Integer.valueOf(maxWordLength), new RandomListColorGenerator(new Color[]{new Color(23, 170, 27), new Color(220, 34, 11), new Color(23, 67, 172)}), new TextDecorator[0]);
        UniColorBackgroundGenerator background = new UniColorBackgroundGenerator(Integer.valueOf(imageWidth), Integer.valueOf(imageHeight), Color.white);
        RandomFontGenerator font = new RandomFontGenerator(Integer.valueOf(fontSize), Integer.valueOf(fontSize), new Font[]{new Font("nyala", 1, fontSize), new Font("Bell MT", 0, fontSize), new Font("Credit valley", 1, fontSize)});
        ImageDeformationByFilters postDef = new ImageDeformationByFilters(new ImageFilter[0]);
        ImageDeformationByFilters backDef = new ImageDeformationByFilters(new ImageFilter[0]);
        ImageDeformationByFilters textDef = new ImageDeformationByFilters(new ImageFilter[0]);
        DeformedComposedWordToImage word2image = new DeformedComposedWordToImage(font, background, randomPaster, backDef, textDef, postDef);
        this.addFactory(new GimpyFactory(wordGenerator, word2image));
    }
}
