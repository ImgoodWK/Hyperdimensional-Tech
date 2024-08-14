package com.imgood.hyperdimensionaltech.geckolib.client.renderer;

import com.imgood.hyperdimensionaltech.geckolib.client.model.HT_Sword_Model;
import software.bernie.example.client.model.item.JackInTheBoxModel;
import software.bernie.example.item.JackInTheBoxItem;
import software.bernie.geckolib3.renderers.geo.GeoItemRenderer;

public class HT_Sword_Renderer extends GeoItemRenderer<JackInTheBoxItem> {
    public HT_Sword_Renderer() {
        super(new HT_Sword_Model());
    }
}
