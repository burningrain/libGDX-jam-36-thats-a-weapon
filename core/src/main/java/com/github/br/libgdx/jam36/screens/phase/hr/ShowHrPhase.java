package com.github.br.libgdx.jam36.screens.phase.hr;

import com.badlogic.gdx.maps.MapGroupLayer;
import com.badlogic.gdx.maps.MapLayer;
import com.github.br.libgdx.jam36.CustomOrthogonalTiledMapRenderer;
import com.github.br.libgdx.jam36.context.GameContext;
import com.github.br.libgdx.jam36.screens.TiledLayers;
import com.github.br.libgdx.jam36.screens.phase.Phase;

public class ShowHrPhase implements Phase {

    @Override
    public void initUI(GameContext gameContext, CustomOrthogonalTiledMapRenderer renderer) {
        MapLayer layer = renderer.getLayer(TiledLayers.DAILY);
        MapGroupLayer mapGroupLayer = (MapGroupLayer) layer;

        MapLayer hr_body = mapGroupLayer.getLayers().get("hr_body");
        hr_body.setVisible(true);

        MapLayer hr = mapGroupLayer.getLayers().get("hr");
        hr.setVisible(true);

        MapLayer hr_right_foot = mapGroupLayer.getLayers().get("hr_right_foot");
        hr_right_foot.setVisible(true);

        MapLayer hr_left_foot = mapGroupLayer.getLayers().get("hr_left_foot");
        hr_left_foot.setVisible(true);
    }

    @Override
    public void draw(float deltaTime) {

    }

    @Override
    public boolean isFinished() {
        return true;
    }

}
