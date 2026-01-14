package com.gmlimsqi.business.basicdata.controller;

public class StandardCurve {
    public static void main(String[] args) {
        // 1. 定义数据
        // x轴：浓度
        double[] x = {0, 1, 2, 5, 10, 15};
        //double[] x = {0.00, 0.08, 0.16, 0.32, 0.48, 0.64};
        double[] y = {0, 0.087, 0.166, 0.423, 0.852, 1.251};

        // y轴：吸光度
        //double[] y = {0.00, 0.092, 0.175, 0.419, 0.886, 1.315};
        //double[] y = {0.00, 0.092, 0.173, 0.419, 0.886, 1.305};
        //double[] y = {0.00, 0.091, 0.181, 0.432, 0.838, 1.191};
        int n = x.length;
        double sumX = 0.0;
        double sumY = 0.0;
        double sumXY = 0.0;
        double sumX2 = 0.0;
        double sumY2 = 0.0;

        // 2. 计算各项累加和
        for (int i = 0; i < n; i++) {
            sumX += x[i];
            sumY += y[i];
            sumXY += x[i] * y[i];
            sumX2 += x[i] * x[i];
            sumY2 += y[i] * y[i];
        }

        // 3. 计算斜率 a 和 截距 b (公式: y = ax + b)
        // a = (nΣxy - ΣxΣy) / (nΣx² - (Σx)²)
        double denominator = n * sumX2 - sumX * sumX;
        if (denominator == 0) {
            System.out.println("分母为0，无法计算");
            return;
        }
        
        double a = (n * sumXY - sumX * sumY) / denominator;
        
        // b = (Σy - aΣx) / n
        double b = (sumY - a * sumX) / n;

        // 4. 计算相关系数 R
        // R = (nΣxy - ΣxΣy) / sqrt([nΣx²-(Σx)²][nΣy²-(Σy)²])
        double numeratorR = n * sumXY - sumX * sumY;
        double denR = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));
        double r = numeratorR / denR;
        double r2 = r * r;

        // 5. 输出结果
        System.out.println("======= 计算结果 =======");
        System.out.printf("斜率 a (Slope): %.6f\n", a);
        System.out.printf("截距 b (Intercept): %.6f\n", b);
        System.out.printf("相关系数 R: %.6f\n", r);
        System.out.printf("决定系数 R²: %.6f\n", r2);
        System.out.println("-----------------------");
        
        String sign = b >= 0 ? "+" : "-";
        System.out.printf("标准曲线方程: y = %.4fx %s %.4f\n", a, sign, Math.abs(b));
        
        // 验证：如果别的 AI 算出的是反的 (x = ay + b)，你可以把 x 和 y 数组互换运行看看
    }
}