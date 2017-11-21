package com.loftschool.moneytracker;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.View;

public class DiagramView extends View {
    private long expense;
    private long income;
    private Paint expensePaint = new Paint();
    private Paint incomePaint = new Paint();

    public DiagramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        expensePaint.setColor(getResources().getColor(R.color.balance_expenses_color));
        incomePaint.setColor(getResources().getColor(R.color.balance_income_color));
        if (isInEditMode()) {
            expense = 15000;
            income = 95000;
        }
    }

    public void update(long expense, long income) {
        this.expense = expense;
        this.income = income;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawPieDiagram(canvas);
        } else {
            drawRectDiagram(canvas);
        }
    }

    private void drawRectDiagram(Canvas canvas) {
        if (expense + income == 0) {
            return;
        }
        long max = Math.max(expense, income);
        long expensesHeight = getHeight() * expense / max;
        long incomeHeight = getHeight() * income / max;
        int w = getWidth() / 4;
        canvas.drawRect(w / 2, getHeight() - expensesHeight, w * 3 / 2, getHeight(), expensePaint);
        canvas.drawRect(5 * w / 2, getHeight() - incomeHeight, w * 7 / 2, getHeight(), incomePaint);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void drawPieDiagram(Canvas canvas) {
        if (expense + income == 0) {
            return;
        }
        float expenseAngle = 360.f * expense / (expense + income);
        float incomeAngle = 360.f * income / (expense + income);
        int space = 10;
        int size = Math.min(getWidth(), getHeight()) - space * 2;
        final int xMargin = (getWidth() - size) / 2;
        final int yMargin = (getHeight() - size) / 2;
        canvas.drawArc(xMargin - space, yMargin, getWidth() - xMargin - space, getHeight() - yMargin,
                180 - expenseAngle / 2, expenseAngle, true, expensePaint);
        canvas.drawArc(xMargin + space, yMargin, getWidth() - xMargin + space, getHeight() - yMargin,
                360 - incomeAngle / 2, incomeAngle, true, incomePaint);
    }
}
