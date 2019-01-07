package filternetfoundation.com.jantamaalik.MoneyJava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import filternetfoundation.com.jantamaalik.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context context){
        this.mContext = context;
    }
    @Override
    public int getCount() {
        return thumbnails.length;
    }

    @Override
    public Object getItem(int i) {
        return thumbnails[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
        ImageView imageView = new ImageView(mContext);
        View gridView;
        if (view==null){
            imageView.setLayoutParams(new ViewGroup.LayoutParams(85, 85));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            return imageView;
            }
          else{
            gridView = inflater.inflate(R.layout.money_fragment_gridview_customlayout, null);
            imageView = gridView.findViewById(R.id.money_frag_grid_item_imagview);
        }
       imageView.setImageResource(thumbnails[i]);
        return imageView;
    }

    private Integer[] thumbnails ={
            R.drawable.aaykar_text, R.drawable.refuelling_nozzle,
            R.drawable.gst_icon, R.drawable.book_icon,
            R.drawable.parle_g_icon };
}
