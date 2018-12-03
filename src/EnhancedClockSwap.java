import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class EnhancedClockSwap {
    private static final boolean ACCESSED = true;
    private static final boolean NOT_ACCESSED = false;
    private static final boolean NOT_MODIFIED = false;
    private static final boolean MODIFIED = true;
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
        this.lastReplaceIndex = -1;
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
        } else /*保证一定会找到*/ {
            /*包含当前页面*/
            if (clockDequeList.contains(pageNew)) {

                pageNew = clockDequeList.get(clockDequeList.lastIndexOf(pageNew));
                pageNew.isAccessed = ACCESSED;
                System.out.println("当前分区号存在：" + pageNew.pageNum + "  正在进行访问位修改");

            } else {
                int replaceIndex = -1;
                do {

                    /*第一步*/
                    for (int i = 0; i < clockDequeList.size(); i++) {
                        Page curPage = clockDequeList.get(i);
                        if (curPage.pageNum != -1 && curPage.isModified == MODIFIED && curPage.isAccessed == NOT_ACCESSED) {
                            replaceIndex = i;
                            break;
                        }
                    }
                    /*第二步*/
                    if (replaceIndex == -1) {
                        for (int i = 0; i < clockDequeList.size(); i++) {
                            Page curPage = clockDequeList.get(i);
                            if (curPage.pageNum != -1 && curPage.isModified == NOT_MODIFIED && curPage.isAccessed == NOT_ACCESSED) {
                                replaceIndex = i;
                                break;
                            }
                            curPage.isAccessed = ACCESSED;
                        }
                    }
                } while (replaceIndex != -1);
                Page replacePage = clockDequeList.get(replaceIndex);
                System.out.println("开始置换分区 : 分区索引:" + replaceIndex + " 分区值为:" + clockDequeList.get(replaceIndex).pageNum + " 状态：  被访问过:" + replacePage.isAccessed + "  被修改过：" + replacePage.isModified);
                clockDequeList.set(replaceIndex, pageNew);
                System.out.println("置换分区完成");
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
        boolean isModified = NOT_MODIFIED;

        public Page(int pageNum) {
            this.pageNum = pageNum;
            this.isAccessed = ACCESSED;
            this.isModified = NOT_MODIFIED;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == null || !(obj instanceof Page))
                return false;
            return this.pageNum == ((Page) obj).pageNum;
        }
    }
}
