package in.filternet.jantamalik.MoneyJava;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import in.filternet.jantamalik.R;

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
            R.drawable.tax_incometax,
            R.drawable.tax_petrol, // https://economictimes.indiatimes.com/news/et-explains/why-you-end-up-paying-almost-double-for-petrol-and-diesel/articleshow/65718645.cms
            R.drawable.tax_gst,
            R.drawable.tax_book,
            R.drawable.tax_parleg // https://www.outlookindia.com/website/story/to-cope-with-gst-rate-hike-parle-g-will-eat-into-your-biscuit-pack/308807
    };
}
