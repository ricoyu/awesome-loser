package com.loserico.pattern.flyweight;
/** 
 *  
 *作者：alaric 
 *时间：2013-7-27下午4:56:01 
 *描述：客户端 为了简单 就直接写main方法里的 
 */  
public class Client {  
  
      
    /** 
     *作者：alaric 
     *时间：2013-7-27下午4:20:08 
     *描述：测试 
     */  
    public static void main(String[] args) {  
        Row r =new Row();  
        GlyphFactory factory = new GlyphFactory();  
        Context context1= new Context(12, 'a');  
        Glyph gly1 = factory.getGlyph(context1);  
        r.setCharacter(gly1);  
          
        Context context2= new Context(13, 'a');  
        Glyph gly2 = factory.getGlyph(context2);  
        r.setCharacter(gly2);  
          
        Context context3= new Context(13, 'b');  
        Glyph gly3 = factory.getGlyph(context3);  
        r.setCharacter(gly3);  
          
        System.out.println(r.getRow());  
          
    }  
}  