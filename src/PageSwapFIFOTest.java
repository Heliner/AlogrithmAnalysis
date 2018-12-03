import java.util.Scanner;

public class PageSwapFIFOTest {
    public static void main(String[] arg) {
        Scanner sc = new Scanner(System.in);
        PageSwapFIFO pageSwapFIFO = new PageSwapFIFO();

        for (; ; ) {
            System.out.println("0 初始化 1 加入一块页面 2 自动运行 3 打印全部页面信息 4 打印缺页率等信息 5 退出 6 随机生成页面序号并置换");
            switch (sc.nextInt()) {
                case 0:
                    pageSwapFIFO.choseAndInit();
                    break;
                case 1:
                    System.out.println("input the pageNum(pageName):");
                    pageSwapFIFO.swapOne(sc.nextInt());
                    break;
                case 2:
                    pageSwapFIFO.automaticRun();
                    break;
                case 3:
                    pageSwapFIFO.printAllMemPage();
                    break;
                case 4:
                    pageSwapFIFO.printLostPageAndRatio();
                    break;
                case 5:
                    System.exit(0);
                    break;
                case 6:
                    pageSwapFIFO.randomPageNumAndSwap();
                    break;
                default:
                    System.out.println("未知输入!");
                    ;

            }
        }
    }
}
