import org.junit.Test;

import java.util.List;

public class TestInheritSwapPage extends BaseSwapPage {
    int lostPage;
    int serialize;

    @Override
    public List<Integer> cachedPage() {
        return null;
    }

    @Override
    public void init(int memPagesSize) {

    }

    @Override
    public void randomSwapPage() {

    }

    @Override
    public void swapOne(Integer pageNum) {

    }

    @Override
    public void printAllMemPage() {

    }

    @Test
    public void test() {
        TestInheritSwapPage testInheritSwapPage = new TestInheritSwapPage();
        testInheritSwapPage.lostPage++;
        testInheritSwapPage.serialize += 2;
        testInheritSwapPage.printLostPageAndRatio();
    }

}
