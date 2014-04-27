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
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import static net.objecthunter.flick3d.Flick3dRenderer.loadShader;

public class Triangle {
    // number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;

    ByteBuffer bb;
    FloatBuffer vertexBuffer;
    float color[] = {0.5f, 0.5f, 0.5f, 1.0f}; // grau
    float coords[] = {0.0f, 0.622008459f, 0.0f,
            -0.5f, -0.311004243f, 0.0f,
            0.5f, -0.311004243f, 0.0f}; // 9 weil 3 punkte mit 3 koordinaten
    int program;
    int positionHandle;
    int colorHandle;
    int mvpHandle;

    // shader GL code der kompiliert wird durch konstruktor aufrufe
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    "  gl_Position = vPosition * uMVPMatrix;" +
                    "}";

    // shader GL code der kompiliert wird durch konstruktor aufrufe
    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";

    public Triangle() {
        // initialize vertex byte buffer for shape coordinates
        bb = ByteBuffer.allocateDirect(
                // (number of coordinate values * 4 bytes per float)
                coords.length * 4);
        // use the device hardware's native byte order
        bb.order(ByteOrder.nativeOrder());
        // create a floating point buffer from the ByteBuffer
        vertexBuffer = bb.asFloatBuffer();
        // add the coordinates to the FloatBuffer
        vertexBuffer.put(coords);
        // set the buffer to read the first coordinate
        vertexBuffer.position(0);

        int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        // initialisiere die shader und das gl program
        program = GLES20.glCreateProgram();             // create empty OpenGL ES Program
        GLES20.glAttachShader(program, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(program, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(program);                  // creates OpenGL ES program executables
    }

    public void draw(float[] mvpMatrix) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(program);

        // get handle to vertex shader's vPosition member
        positionHandle = GLES20.glGetAttribLocation(program, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(positionHandle);

        // Prepare the triangle coordinate data
        GLES20.glVertexAttribPointer(positionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                COORDS_PER_VERTEX * 4, vertexBuffer);

        // get handle to fragment shader's vColor member
        colorHandle = GLES20.glGetUniformLocation(program, "vColor");

        // Set color for drawing the triangle
        GLES20.glUniform4fv(colorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        mvpHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            throw new RuntimeException("glGetUniformLocation: glError " + error);
        }

        // Apply the projection and view transformation
        Log.v("Flick3D", mvpMatrix[0] + " " + mvpMatrix[3] + " " + mvpMatrix[6]);
        Log.v("Flick3D", mvpMatrix[1] + " " + mvpMatrix[4] + " " + mvpMatrix[7]);
        Log.v("Flick3D", mvpMatrix[2] + " " + mvpMatrix[5] + " " + mvpMatrix[8]);
        GLES20.glUniformMatrix4fv(mvpHandle, 1, false, mvpMatrix, 0);
        Log.v("Flick3D", mvpMatrix[0] + " " + mvpMatrix[3] + " " + mvpMatrix[6]);
        Log.v("Flick3D", mvpMatrix[1] + " " + mvpMatrix[4] + " " + mvpMatrix[7]);
        Log.v("Flick3D", mvpMatrix[2] + " " + mvpMatrix[5] + " " + mvpMatrix[8]);
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            throw new RuntimeException("glUniformMatrix4fv: glError " + error);
        }

        // Draw the triangle
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(positionHandle);
    }

}