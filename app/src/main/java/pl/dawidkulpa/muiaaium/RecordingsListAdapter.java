package pl.dawidkulpa.muiaaium;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RecordingsListAdapter extends ArrayAdapter<Recording> {

    private ArrayList<Recording> data;
    private Context context;
    private int selected;

    public RecordingsListAdapter(@NonNull Context context, ArrayList<Recording> data) {
        super(context, R.layout.recording_list_item);
        this.context= context;
        this.data= data;
        selected=-1;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull final ViewGroup parent) {
        View row= convertView;
        RecordingHolder recordingHolder;
        Recording currentObj= data.get(position);

        if(row==null){
            LayoutInflater inflater= ((Activity) context).getLayoutInflater();
            row= inflater.inflate(R.layout.recording_list_item, null);

            recordingHolder= new RecordingHolder();
            recordingHolder.nameText= row.findViewById(R.id.name_text);
            recordingHolder.lengthText= row.findViewById(R.id.length_text);

            row.setTag(recordingHolder);
        }else {
            recordingHolder= (RecordingHolder) row.getTag();
        }


        recordingHolder.nameText.setText(currentObj.getName());

        long len= currentObj.getLength();
        String lenStr= String.valueOf(len/60000);
        lenStr+=":";
        if( (len/1000)%60<10 )
            lenStr+="0";
        lenStr+= String.valueOf( (len/1000)%60 );
        recordingHolder.lengthText.setText(lenStr);

        if(selected==position)
            row.setBackgroundColor(context.getResources().getColor(R.color.selectedRecordingColor));
        else
            row.setBackgroundColor(context.getResources().getColor(R.color.notSelectedRecordingColor));

        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selected==position)
                    selected=-1;
                else
                    selected= position;
                notifyDataSetInvalidated();
            }
        });

        return row;
    }

    @Nullable
    @Override
    public Recording getItem(int position) {
        return data.get(position);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    public int getSelectedItem() {
        return selected;
    }

    public void setSelectedItem(int idx) {
        if(idx<data.size())
            this.selected = idx;
        else
            this.selected= -1;
    }

    static class RecordingHolder{
        TextView nameText;
        TextView lengthText;
    }
}
