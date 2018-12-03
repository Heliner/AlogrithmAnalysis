import java.util.List;

public abstract class BaseSwapPage {

    public int lostPage;
    public int serialize;

    public abstract List<Integer> cachedPage();

    public abstract void init(int memPagesSize);

    public abstract void randomSwapPage();

    public abstract void swapOne(Integer pageNum);

    public abstract void printAllMemPage();


    public  void printLostPageAndRatio(){
        System.out.println();
        System.out.println("缺页数:" + this.lostPage);
        System.out.println("缺页率:" + (1f * this.lostPage / this.serialize * 100 + "%"));
        System.out.println();

    };
}
