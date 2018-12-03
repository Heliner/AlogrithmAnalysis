import org.junit.Test;

import java.util.*;

public class LFUSwap {
    LinkedList<Page> lfuQueue = new LinkedList<>();
    Page EMPTY_PAGE = new Page(-1);
    //页面大小
    int memPagesSize;
    //用于记录当前页块的版本号
    int serialize = 0;
    //缺页个数
    int lostPage = 0;

    void init(int memPagesSize) {
        if (memPagesSize <= 0)
            return;
        lfuQueue.clear();
        this.memPagesSize = memPagesSize;
        lostPage = 0;
        serialize = 0;
        for (int i = 0; i < memPagesSize; i++) {
            lfuQueue.add(EMPTY_PAGE);
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

        Page insertPage = new Page(memPageNum);
        /*替换空闲空间*/
        if (lfuQueue.contains(EMPTY_PAGE)) {
            lostPage++;
            lfuQueue.set(lfuQueue.indexOf(EMPTY_PAGE),insertPage);
            System.out.println("置换空闲页面");

        }else{
            /*如果包含此元素频率增加，并且进行排序*/
            if(lfuQueue.contains(insertPage)){
                int index = lfuQueue.indexOf(insertPage);
                Page oldPage = lfuQueue.get(index);
                oldPage.frequent++;
                /*按频率进行排序*/
                Collections.sort(lfuQueue,new PageComparetor());
                System.out.println("0"+lfuQueue.get(0).frequent);
                System.out.println("1"+lfuQueue.get(1).frequent);
            }else/*移除队尾元素*/{

                /*移除最后一个元素,然后插入信息的页面*/
                Page oldPage = lfuQueue.get(memPagesSize-1);
                lfuQueue.set(memPagesSize-1,insertPage);
                System.out.println("需要进行页面置换，：原页面索引："+(memPagesSize-1)+" 编号："+oldPage.pageNum +" 频率:"+oldPage.frequent);

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
        for (int i = 0; i < lfuQueue.size(); i++) {
            System.out.println("index " + i + "  pageNum : " + lfuQueue.get(i));
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
        int frequent = 0;
        int pageNum = 0;

        public Page(int pageNum) {
            this.pageNum = pageNum;
            this.frequent = 1;
        }
    }

    class PageComparetor implements Comparator<Page> {

        @Override
        public int compare(Page o1, Page o2) {
            return -(o1.frequent-o2.frequent);
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }
}
