package filternetfoundation.com.jantamaalik.IssuesJava;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import filternetfoundation.com.jantamaalik.R;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.IssuesViewHolder> {
     private List<String> dataList = new ArrayList<>();

    public static class IssuesViewHolder extends RecyclerView.ViewHolder {

        public TextView mtextView;

        public IssuesViewHolder(View itemView) {
            super(itemView);
            mtextView = itemView.findViewById(R.id.Issues_recyclerview_textView);
        }
    }
    public MyAdapter(String[] dataArray){
        for(int i=0;i<dataArray.length;i++){
            dataList.add(dataArray[i]);
        }

    }
    @NonNull
    @Override
    public MyAdapter.IssuesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.issues_row_item_layout,parent,false);

          IssuesViewHolder issuesViewHolder = new IssuesViewHolder(view);
        return issuesViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull IssuesViewHolder holder, int position) {
          holder.mtextView.setText(dataList.get(position));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }




}
