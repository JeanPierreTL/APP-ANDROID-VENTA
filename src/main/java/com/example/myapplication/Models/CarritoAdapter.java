package com.example.myapplication.Models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.R;

import java.text.DecimalFormat;
import java.util.List;

public class CarritoAdapter extends BaseAdapter {
    private Context context;
    private List<ItemCarrito> items;
    private OnCarritoChangeListener carritoChangeListener; // Listener para cambios en el carrito

    public CarritoAdapter(Context context, List<ItemCarrito> items, OnCarritoChangeListener carritoChangeListener) {
        this.context = context;
        this.items = items;
        this.carritoChangeListener = carritoChangeListener;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflar el diseño del ítem si es necesario
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_carrito, parent, false);
        }

        // Obtener el producto actual
        ItemCarrito item = items.get(position);

        // Configurar los elementos de la UI
        ImageView productImage = convertView.findViewById(R.id.productImage);
        TextView productName = convertView.findViewById(R.id.productName);
        TextView productPrice = convertView.findViewById(R.id.productPrice);
        TextView productQuantity = convertView.findViewById(R.id.productQuantity);
        TextView productTotalPrice = convertView.findViewById(R.id.productTotalPrice); // Nuevo para el precio total
        ImageButton increaseQuantity = convertView.findViewById(R.id.increaseQuantity);
        ImageButton decreaseQuantity = convertView.findViewById(R.id.decreaseQuantity);

        // Configurar los valores
        productName.setText(item.getProducto().getNombreProducto());
        productPrice.setText("S/ " + item.getProducto().getPrecio());
        productQuantity.setText(String.valueOf(item.getCantidad()));
        actualizarPrecioTotal(productTotalPrice, item);

        // Cargar imagen del producto si es necesario
        int imageResId = context.getResources().getIdentifier(item.getProducto().getImagen(), "drawable", context.getPackageName());
        productImage.setImageResource(imageResId);

        // Manejar eventos de los botones
        increaseQuantity.setOnClickListener(v -> {
            item.setCantidad(item.getCantidad() + 1);
            actualizarPrecioTotal(productTotalPrice, item); // Actualizar el precio total del producto
            notifyDataSetChanged();
            if (carritoChangeListener != null) {
                carritoChangeListener.onCarritoChange(); // Notificar cambio al carrito
            }
        });

        decreaseQuantity.setOnClickListener(v -> {
            if (item.getCantidad() > 1) {
                item.setCantidad(item.getCantidad() - 1);
                actualizarPrecioTotal(productTotalPrice, item); // Actualizar el precio total del producto
                notifyDataSetChanged();
                if (carritoChangeListener != null) {
                    carritoChangeListener.onCarritoChange(); // Notificar cambio al carrito
                }
            }
        });

        return convertView;
    }

    private void actualizarPrecioTotal(TextView productTotalPrice, ItemCarrito item) {
        double total = item.getCantidad() * item.getProducto().getPrecio();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        productTotalPrice.setText("Total: S/ " + decimalFormat.format(total));
    }

    public interface OnCarritoChangeListener {
        void onCarritoChange();
    }
}
