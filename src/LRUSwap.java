import org.junit.Test;

import java.util.*;

public class LRUSwap {
    LinkedList<Integer> lruQueue = new LinkedList<>();
    static final Integer EMPTY_PLACE = -1;
    //页面大小
    int memPagesSize;
    //用于记录当前页块的版本号
    int serialize = 0;
    //缺页个数
    int lostPage = 0;

    void init(int memPagesSize) {
        if (memPagesSize <= 0)
            return;
        lruQueue.clear();
        this.memPagesSize = memPagesSize;
        lostPage = 0;
        serialize = 0;
        for (int i = 0; i < memPagesSize; i++) {
            lruQueue.add(EMPTY_PLACE);
        }

    }

    //置换
    void swapOne(int memPageNum) {
        System.out.println("");
        System.out.println("正在请求置换页面:" + memPageNum);
        serialize++;
        selectFIPage(memPageNum);
        System.out.println("页面置换完成");
        System.out.println();
    }

    //选择策略
    private void selectFIPage(int memPageNum) {
        //没有全部覆盖
        boolean tokeUpAll = true;
        int indexReplace = -1;
        if (lruQueue.contains(EMPTY_PLACE)) {
            lostPage++;
            indexReplace = lruQueue.indexOf(EMPTY_PLACE);
            System.out.println("页面:" + indexReplace + " 为空闲 直接进行替换");
            lruQueue.set(lruQueue.indexOf(EMPTY_PLACE), memPageNum);
        } else {
            /*判断是否重复*/
            /*移动到队尾*/
            if (lruQueue.contains(memPageNum)) {
                indexReplace = lruQueue.indexOf(memPageNum);
                lruQueue.remove(lruQueue.indexOf(memPageNum));
                lruQueue.addLast(memPageNum);
                System.out.println("当前页面存在 ；将其移动到队尾");
            } else {
                lostPage++;
                int oldVal = lruQueue.removeFirst();
                System.out.println("移除第一个元素 值为：" + oldVal);
                lruQueue.addLast(memPageNum);
                System.out.println("将" + memPageNum + "添加到队尾");
            }
        }
    }

    @Test
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
        for (int i = 0; i < lruQueue.size(); i++) {
                System.out.println("index " + i + "  pageNum : " +lruQueue.get(i));
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
