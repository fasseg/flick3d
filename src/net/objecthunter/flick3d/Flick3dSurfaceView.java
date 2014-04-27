/* 
* Copyright 2014 Frank Asseg
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License. 
*/
package net.objecthunter.flick3d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class Flick3dSurfaceView extends GLSurfaceView {

    public Flick3dSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        setRenderer(new Flick3dRenderer());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY); // nur rendern wenn requestRender aufgerufen wird()
    }
}
