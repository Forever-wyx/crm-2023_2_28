import org.junit.Test;

import java.util.ResourceBundle;

public class PropertiesTest {

    @Test
    public void test(){
        ResourceBundle bundle = ResourceBundle.getBundle("possibility");
        String ret = bundle.getString("资质审查");
        System.out.println(ret);
    }
}
