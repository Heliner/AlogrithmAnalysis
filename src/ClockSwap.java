import org.junit.Test;

import java.util.*;

public class ClockSwap {
    private static final boolean ACCESSED = true;
    private static final boolean NOT_ACCESSED = false;
    List<Page> clockDequeList = new ArrayList<>();

    static final Page FREE_PAGE = new Page(-1);
    int lastReplaceIndex = -1;
    //页面数量
    int memPagesSize;
    //用于记录当前页块的版本号
    int serialize = 0;
    //缺页个数
    int lostPage = 0;

    void init(int memPagesSize) {
        if (memPagesSize <= 0)
            return;
        this.lastReplaceIndex =-1;
        this.memPagesSize = memPagesSize;
        lostPage = 0;
        serialize = 0;
        clockDequeList.clear();
        for (int i = 0; i < memPagesSize; i++) {
            clockDequeList.add(FREE_PAGE);
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
        if (memPageNum < 0)
            return;
        /*包含-1*/
        Page pageNew = new Page(memPageNum);
        if (clockDequeList.contains(FREE_PAGE)) {
            lostPage++;
            int freeIndex = clockDequeList.indexOf(FREE_PAGE);

            System.out.println("当前页面存在空闲分区 : 分区索引：" + freeIndex);
            clockDequeList.set(freeIndex, pageNew);
            lastReplaceIndex++;
            System.out.println("置换空闲分区:" + freeIndex + "结束");
        } else {

            /*当前分区存在,不改变久指针的位置*/
            int replaceIndex = -1;
            if (clockDequeList.contains(pageNew)) {
                System.out.println("分区号为:" + memPageNum + " 的分区已存在，改变标志位为1");
                Page oldPage = clockDequeList.get(clockDequeList.indexOf(pageNew));
                oldPage.isAccessed = ACCESSED;
            } else {
                lostPage++;
                System.out.println("开始遍历寻找空闲的分区");
                int oneRound = this.memPagesSize + 1;
                while (oneRound > 0) {
                    lastReplaceIndex++;
                    lastReplaceIndex %= memPagesSize;

                    Page curPage = clockDequeList.get(lastReplaceIndex);
                    if (curPage.isAccessed == ACCESSED) {
                        curPage.isAccessed = NOT_ACCESSED;
                        System.out.println("设置页面:" + curPage.pageNum + " 状态为：未访问");
                    } else {
                        replaceIndex = lastReplaceIndex;
                        clockDequeList.set(replaceIndex, pageNew);
                        System.out.println("替换索引:" + replaceIndex + " 原来值为:" + curPage.pageNum + " 为:" + pageNew.pageNum);
                        break;
                    }

                    oneRound--;
                }

            }

            if (replaceIndex == -1) {
                System.out.println("出现错误");
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
        for (int i = 0; i < clockDequeList.size(); i++) {
            System.out.println("当前页面索引：" + i + " 值为:" + clockDequeList.get(i).pageNum + " 被访问：" + clockDequeList.get(i).isAccessed);
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

    static class Page {
        int pageNum;
        boolean isAccessed = ACCESSED;

        public Page(int pageNum) {
            this.pageNum = pageNum;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Page))
                return false;
            return this.pageNum == ((Page) obj).pageNum;
        }
    }
}
