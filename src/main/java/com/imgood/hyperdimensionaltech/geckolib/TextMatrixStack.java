package com.imgood.hyperdimensionaltech.geckolib;
import java.nio.FloatBuffer;
import java.util.Stack;
import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import org.lwjgl.BufferUtils;

public class TextMatrixStack {
    private Stack<Matrix4f> modelStack = new Stack<>();
    private Matrix4f tempModelMatrix = new Matrix4f();

    public TextMatrixStack() {
        Matrix4f initialModelMatrix = new Matrix4f();
        initialModelMatrix.setIdentity();
        modelStack.push(initialModelMatrix);
    }

    public Matrix4f getCurrentMatrix() {
        return modelStack.peek();
    }

    public void push() {
        modelStack.push(new Matrix4f(modelStack.peek()));
    }

    public void pop() {
        if (modelStack.size() == 1) {
            throw new IllegalStateException("Stack cannot be popped below the initial level.");
        } else {
            modelStack.pop();
        }
    }

    public void translate(float x, float y, float z) {
        this.translate(new Vector3f(x, y, z));
    }

    public void translate(Vector3f vec) {
        tempModelMatrix.setIdentity();
        tempModelMatrix.setTranslation(vec);
        modelStack.peek().mul(tempModelMatrix);
    }

    public void scale(float x, float y, float z) {
        tempModelMatrix.setIdentity();
        tempModelMatrix.m00 = x;
        tempModelMatrix.m11 = y;
        tempModelMatrix.m22 = z;
        modelStack.peek().mul(tempModelMatrix);
    }

    public void rotateX(float radian) {
        tempModelMatrix.setIdentity();
        tempModelMatrix.rotX(radian);
        modelStack.peek().mul(tempModelMatrix);
    }

    public void rotateY(float radian) {
        tempModelMatrix.setIdentity();
        tempModelMatrix.rotY(radian);
        modelStack.peek().mul(tempModelMatrix);
    }

    public void rotateZ(float radian) {
        tempModelMatrix.setIdentity();
        tempModelMatrix.rotZ(radian);
        modelStack.peek().mul(tempModelMatrix);
    }

    public void applyToGL() {
        FloatBuffer buffer = convertMatrixToFloatBuffer(getCurrentMatrix());
        org.lwjgl.opengl.GL11.glMultMatrix(buffer);
    }

    private FloatBuffer convertMatrixToFloatBuffer(Matrix4f matrix) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(16);
        buffer.put(matrix.m00);
        buffer.put(matrix.m01);
        buffer.put(matrix.m02);
        buffer.put(matrix.m03);
        buffer.put(matrix.m10);
        buffer.put(matrix.m11);
        buffer.put(matrix.m12);
        buffer.put(matrix.m13);
        buffer.put(matrix.m20);
        buffer.put(matrix.m21);
        buffer.put(matrix.m22);
        buffer.put(matrix.m23);
        buffer.put(matrix.m30);
        buffer.put(matrix.m31);
        buffer.put(matrix.m32);
        buffer.put(matrix.m33);
        buffer.flip(); // 准备数据进行读取
        return buffer;
    }
}
