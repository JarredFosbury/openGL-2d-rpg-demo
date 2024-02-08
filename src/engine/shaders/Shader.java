package engine.shaders;

import org.joml.*;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader
{
    private int shaderProgramID;
    private String filepath;

    public int getShaderProgramID()
    {
        return shaderProgramID;
    }

    public String getFilepath()
    {
        return filepath;
    }

    public Shader(String filepath)
    {
        this.filepath = filepath;
        shaderProgramID = ShaderCompiler.compileShader(filepath);
    }

    public void bind()
    {
        glUseProgram(shaderProgramID);
    }

    public void unbind()
    {
        glUseProgram(0);
    }

    public void updateUniforms()
    {}

    //TODO: cache these glGetUniformLocation calls somehow, pulling information from the GPU is expensive!!!
    protected void setIntegerUniform(String name, int value)
    {
        glUniform1i(glGetUniformLocation(shaderProgramID, name), value);
    }

    protected void setFloatUniform(String name, float value)
    {
        glUniform1f(glGetUniformLocation(shaderProgramID, name), value);
    }

    protected void setFloat2Uniform(String name, Vector2f value)
    {
        glUniform2f(glGetUniformLocation(shaderProgramID, name), value.x, value.y);
    }

    protected void setFloat3Uniform(String name, Vector3f value)
    {
        glUniform3f(glGetUniformLocation(shaderProgramID, name), value.x, value.y, value.z);
    }

    protected void setFloat4Uniform(String name, Vector4f value)
    {
        glUniform4f(glGetUniformLocation(shaderProgramID, name), value.x, value.y, value.z, value.w);
    }

    protected void setMatrix4Uniform(String name, Matrix4f value)
    {
        // TODO: Verify that the matrix orientation is indeed transpose!

        FloatBuffer matrixValuesBuffer = BufferUtils.createFloatBuffer(16);
        value.get(matrixValuesBuffer);
        glUniformMatrix4fv(glGetUniformLocation(shaderProgramID, name), false, matrixValuesBuffer);
    }
}