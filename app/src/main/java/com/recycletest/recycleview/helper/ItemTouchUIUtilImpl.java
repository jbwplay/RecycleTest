/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.recycletest.recycleview.helper;

import android.graphics.Canvas;
import android.support.v4.view.ViewCompat;
import com.recycletest.recycleview.ResourceUtils;
import com.recycletest.recycleview.RecyclerView;
import android.view.View;

/**
 * Package private class to keep implementations. Putting them inside ItemTouchUIUtil
 * makes them public API, which is not desired in this case.
 */
class ItemTouchUIUtilImpl {
    static class Api21Impl extends BaseImpl {
        @Override
        public void onDraw(Canvas c, RecyclerView recyclerView, View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            if (isCurrentlyActive) {
                Object originalElevation = view.getTag(ResourceUtils.id(recyclerView.getContext(), "item_touch_helper_previous_elevation", ResourceUtils.TYPE.ID));
                if (originalElevation == null) {
                    originalElevation = ViewCompat.getElevation(view);
                    float newElevation = 1f + findMaxElevation(recyclerView, view);
                    ViewCompat.setElevation(view, newElevation);
                    view.setTag(ResourceUtils.id(recyclerView.getContext(), "item_touch_helper_previous_elevation", ResourceUtils.TYPE.ID), originalElevation);
                }
            }
            super.onDraw(c, recyclerView, view, dX, dY, actionState, isCurrentlyActive);
        }

        private float findMaxElevation(RecyclerView recyclerView, View itemView) {
            final int childCount = recyclerView.getChildCount();
            float max = 0;
            for (int i = 0; i < childCount; i++) {
                final View child = recyclerView.getChildAt(i);
                if (child == itemView) {
                    continue;
                }
                final float elevation = ViewCompat.getElevation(child);
                if (elevation > max) {
                    max = elevation;
                }
            }
            return max;
        }

        @Override
        public void clearView(View view) {
            final Object tag = view.getTag(ResourceUtils.id(view.getContext(), "item_touch_helper_previous_elevation", ResourceUtils.TYPE.ID));
            if (tag != null && tag instanceof Float) {
                ViewCompat.setElevation(view, (Float) tag);
            }
            view.setTag(ResourceUtils.id(view.getContext(), "item_touch_helper_previous_elevation", ResourceUtils.TYPE.ID), null);
            super.clearView(view);
        }
    }

    static class BaseImpl implements ItemTouchUIUtil {

        @Override
        public void clearView(View view) {
            view.setTranslationX(0f);
            view.setTranslationY(0f);
        }

        @Override
        public void onSelected(View view) {

        }

        @Override
        public void onDraw(Canvas c, RecyclerView recyclerView, View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            view.setTranslationX(dX);
            view.setTranslationY(dY);
        }

        @Override
        public void onDrawOver(Canvas c, RecyclerView recyclerView, View view, float dX, float dY, int actionState, boolean isCurrentlyActive) {

        }
    }
}
