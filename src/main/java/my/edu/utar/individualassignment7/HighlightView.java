package my.edu.utar.individualassignment7;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.view.View;

public class HighlightView extends View {
    public boolean isHighlighted;

    public HighlightView(Context context) {
        super(context);
        isHighlighted = false;
    }

    public void highlight() {
        isHighlighted = true;
        invalidate();
    }

    public void unhighlight() {
        isHighlighted = false;
        invalidate();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isHighlighted) {
            canvas.drawColor(Color.RED);
        } else {
            canvas.drawColor(Color.GRAY);
        }
    }


}
