import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class PageSwapFIFO {
    int[] memPages;
    int[] memPagesSquentialId;
    //页面大小
    int memPagesSize;
    //用于记录当前页块的版本号
    int serialize = 0;
    //缺页个数
    int lostPage = 0;

    void init(int memPagesSize) {
        if (memPagesSize <= 0)
            return;
        this.memPagesSize = memPagesSize;
        lostPage = 0;
        serialize = 0;
        memPages = new int[memPagesSize];
        memPagesSquentialId = new int[memPagesSize];
        Arrays.fill(memPages, -1);
        Arrays.fill(memPagesSquentialId, -1);
    }

    //置换
    void swapOne(int memPageNum) {
        System.out.println("");
        System.out.println("正在请求置换页面:" + memPageNum);
        serialize++;
        int indexReplace = selectFIPage(memPageNum);
        if (indexReplace == -1) {
            System.out.println("出现内部错误");
            return;
        }
        if (indexReplace == -2) {
            return;
        }
        System.out.println();
        System.out.println("置换页面的索引:" + indexReplace);
        System.out.println();

        memPages[indexReplace] = memPageNum;
        memPagesSquentialId[indexReplace] = serialize;
        System.out.println("页面置换完成");
        System.out.println();
    }

    //选择策略
    private int selectFIPage(int memPageNum) {
        //没有全部覆盖
        boolean tokeUpAll = true;
        int i = 0;
        for (i = 0; i < memPages.length; i++) {
            if (memPages[i] == -1) {
                tokeUpAll = false;
                break;
            }
        }
        if (!tokeUpAll) {
            System.out.println("发生缺页");
            lostPage++;
            return i;
        }
        //判断是否存在这样的分区
        for (int memPage : memPages) {
            if (memPage == memPageNum) {
                System.out.println("当前内存块已存在");
                return -2;
            }
        }
        int minSerialzeId = Integer.MAX_VALUE;
        int minIndex = -1;
        for (i = 0; i < memPages.length; i++) {

            if (memPagesSquentialId[i] < minSerialzeId) {
                minIndex = i;
                minSerialzeId = memPagesSquentialId[i];
            }

        }
        if (minIndex != -1) {
            System.out.println("发生缺页");
            lostPage++;
        }
        return minIndex;
    }

    public void automaticRun() {
        init(3);
        swapOne(7);
        swapOne(0);
        swapOne(1);
        swapOne(2);
        swapOne(0);
        swapOne(3);
        swapOne(0);
        swapOne(4);
        swapOne(2);
        swapOne(3);
        swapOne(0);
        swapOne(3);
        swapOne(2);
        swapOne(1);
        swapOne(2);
        swapOne(0);
        swapOne(1);
        swapOne(7);
        swapOne(0);
        swapOne(1);

        printLostPageAndRatio();
    }

    public void choseAndInit() {
        Scanner sc = new Scanner(System.in);
        System.out.println("input the page size :");
        int pageSize = sc.nextInt();
        init(pageSize);
    }

    public void printAllMemPage() {
        System.out.println();
        for (int i = 0; i < memPages.length; i++) {
            if (memPages[i] != -1) {
                System.out.println("index " + i + "  pageNum : " + memPages[i]);
            }
        }
        System.out.println();

    }

    public void printLostPageAndRatio() {

        System.out.println();
        System.out.println("缺页数:" + this.lostPage);
        System.out.println("缺页率:" + (1f * this.lostPage / this.serialize * 100 + "%"));
        System.out.println();
    }

    public void randomPageNumAndSwap() {
        System.out.println("正在随机生成页面号");
        Random random = new Random();
        int rand = random.nextInt(this.memPagesSize);
        System.out.println("生成页面号为 :" + rand);
        swapOne(rand);
    }
}
