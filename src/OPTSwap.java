import org.junit.Test;

import java.util.*;

public class OPTSwap {
    /*用于显示当前缓存中的页面*/
    List<Integer> memPageCache = new ArrayList<>();
    /*指向当前需要判断的页面*/
    int memPagePointer = -1;
    /*全部页面*/
    int[] totalPage;
    //缓存大小
    int memPagesSize;
    //用于记录当前页块的版本号
    int serialize = 0;
    //缺页个数
    int lostPage = 0;
    List<Integer> lastOccurent = new ArrayList<>();
    static final int NOT_OCC_YET = 0;
    static final int FREE_PAGE = -1;

    void init(int memPagesSize, int[] totalPage) {
        memPageCache.clear();
        lastOccurent.clear();

        Scanner sc = new Scanner(System.in);
        if (memPagesSize <= 0)
            return;

        this.memPagesSize = memPagesSize;
        lostPage = 0;
        serialize = 0;
        this.totalPage = totalPage;

        for (int i = 0; i < memPagesSize; i++) {
            memPageCache.add(FREE_PAGE);
            /*lastOccurent.add(NOT_OCC_YET);*/
        }
    }

    //选择策略
    private int selectFIPage(int newMemPageNum) {
        /*有空闲分区*/
        memPagePointer++;
        if (memPageCache.contains(FREE_PAGE)) {
            lostPage++;
            memPageCache.set(memPageCache.indexOf(FREE_PAGE), newMemPageNum);
        } else /*更新往后最近出现的时机*/ {
            /*当前缓存中存在该值*/
            if (memPageCache.contains(newMemPageNum)) {
                /*什么都不做*/
            } else/*当前缓存不包含对应的值*/ {
                /*遍历*/
                lastOccurent.clear();
                for (int i = memPagePointer; i < totalPage.length; i++) {
                    boolean flag = false;
                    int tCurMemPage = -1;
                    for (int curMemPageCache : memPageCache) {
                        if (curMemPageCache == totalPage[i]) {
                            flag = true;
                            tCurMemPage = curMemPageCache;
                            break;
                        }
                    }
                    if (flag == true) {
                        lastOccurent.add(tCurMemPage);
                    }
                    if (lastOccurent.size() == memPagesSize) {
                        break;
                    }
                }
                /*如果一个都没有出现就 随意替换*/
                int replacePageNum = 0;
                if (lastOccurent.size() == 0) {
                    replacePageNum = memPageCache.get(0);
                } else {
                    replacePageNum = lastOccurent.get(0);
                }
                if (memPageCache.contains(replacePageNum)) {
                    memPageCache.set(memPageCache.indexOf(replacePageNum), newMemPageNum);
                } else {
                    System.out.println("错误----------------");
                }
            }
        }
        return -1;
    }

    @Test
    public void automaticRun() {
        init(3, new int[]{7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1});
        for (int pageNum : totalPage) {
            swapOne(pageNum);
        }
        printLostPageAndRatio();

    }

    private void swapOne(int memPageNum) {
        System.out.println("");
        System.out.println("正在请求置换页面:" + memPageNum);
        serialize++;
        selectFIPage(memPageNum);
        System.out.println("页面置换完成");
        System.out.println();
    }

    public void choseAndInit() {
        Scanner sc = new Scanner(System.in);
        System.out.println("input the page size :");
        int pageSize = sc.nextInt();
        init(pageSize, new int[]{7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2, 1, 2, 0, 1, 7, 0, 1});
    }

    public void printAllMemPage() {
        System.out.println();
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
