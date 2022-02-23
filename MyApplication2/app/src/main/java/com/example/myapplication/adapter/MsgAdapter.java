package com.example.myapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.example.myapplication.bean.Mensaje;
import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Mensaje> mMensajeList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;

        public ViewHolder(View itemView) {
            super(itemView);
            leftLayout = itemView.findViewById(R.id.left_layout);
            rightLayout = itemView.findViewById(R.id.right_layout);
            leftMsg = itemView.findViewById(R.id.left_msg);
            rightMsg = itemView.findViewById(R.id.right_msg);

        }
    }

    public MsgAdapter(List<Mensaje> mMensajeList) {
        this.mMensajeList = mMensajeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Mensaje mensaje = mMensajeList.get(position);
        if (mensaje.getType() == Mensaje.TYPE_RECEIVED) {
            // Si es un mensaje recibido, muestre el diseño del mensaje a la izquierda y oculte el diseño del mensaje a la derecha
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(mensaje.getContent());
        } else if (mensaje.getType() == Mensaje.TYPE_SENT) {
            // Si es un mensaje enviado, muestra el diseño del mensaje a la derecha y oculta el diseño del mensaje a la izquierda
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(mensaje.getContent());
        }

    }

    @Override
    public int getItemCount() {
        return mMensajeList.size();
    }

}