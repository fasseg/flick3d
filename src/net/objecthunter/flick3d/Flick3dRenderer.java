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

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Flick3dRenderer implements GLSurfaceView.Renderer {
    private Triangle triangle;

    private float angle;
    private final float[] projectionMatrix = new float[16];
    private final float[] mMVPMatrix = new float[16]; // transformations ziel
    private final float[] mVMatrix = new float[16]; // view (Kamera position)
    private final float[] mRotationMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        triangle = new Triangle();
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        float aspectRatio = (float) width / height;
        Matrix.frustumM(projectionMatrix, 0, -aspectRatio, aspectRatio, -1, 1, 3, 7);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

        // die kamera einstellen
        Matrix.setLookAtM(mVMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);
        // die transformation mit den positions vektoren des dreiceks multiplizieren
        Matrix.multiplyMM(mMVPMatrix, 0, projectionMatrix, 0, mVMatrix, 0);

        // und eine rotation dranhängen
        angle+=1;
        Matrix.setRotateM(mRotationMatrix, 0, angle, 0, 0, -1.0f);
        // und dann mit den positions vektoren des dreiecks multiplizieren
        Matrix.multiplyMM(mMVPMatrix, 0, mRotationMatrix, 0, mMVPMatrix, 0);

        triangle.draw(mMVPMatrix);
        Log.v("Flick3D", "completed render cycle");
    }

    public static int loadShader(int type, String shaderCode){
        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }
}
