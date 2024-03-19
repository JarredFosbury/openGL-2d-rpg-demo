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
    protected void setIntegerUniform(int location, int value)
    {
        glUniform1i(location, value);
    }

    protected void setFloatUniform(int location, float value)
    {
        glUniform1f(location, value);
    }

    protected void setFloat2Uniform(int location, Vector2f value)
    {
        glUniform2f(location, value.x, value.y);
    }

    protected void setFloat3Uniform(int location, Vector3f value)
    {
        glUniform3f(location, value.x, value.y, value.z);
    }

    protected void setFloat4Uniform(int location, Vector4f value)
    {
        glUniform4f(location, value.x, value.y, value.z, value.w);
    }

    protected void setMatrix4Uniform(int location, Matrix4f value)
    {
        FloatBuffer matrixValuesBuffer = BufferUtils.createFloatBuffer(16);
        value.get(matrixValuesBuffer);
        glUniformMatrix4fv(location, false, matrixValuesBuffer);
    }

    public void delete()
    {
        unbind();
        glDeleteProgram(shaderProgramID);
    }
}