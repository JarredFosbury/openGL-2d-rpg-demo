package engine.shaders;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import static org.lwjgl.opengl.GL20.*;

public class ShaderCompiler
{
    public static int compileShader(String filepath)
    {
        String vertexSource;
        String fragmentSource;

        int vertShaderID;
        int fragShaderID;
        int shaderProgramID;

        try
        {
            File sourceFile = new File(filepath);
            BufferedReader reader = new BufferedReader(new FileReader(sourceFile));

            StringBuilder vertexBuilder = new StringBuilder();
            StringBuilder fragmentBuilder = new StringBuilder();

            int sourceType = 0; // 0 = undefined, 1 = vertex, 2 = fragment

            String currentLine;
            while ((currentLine = reader.readLine()) != null)
            {
                if (currentLine.equals("#type vertex"))
                {
                    sourceType = 1;
                    continue;
                }
                else if (currentLine.equals("#type fragment"))
                {
                    sourceType = 2;
                    continue;
                }
                else if (sourceType == 0)
                {
                    System.err.println("FILE IO ERROR: No valid GLSL shader types found!\nPlease use \"#type vertex\" or \"#type fragment\" to define a shader type!");
                }

                if (sourceType == 1)
                    vertexBuilder.append(currentLine).append("\n");
                else
                    fragmentBuilder.append(currentLine).append("\n");
            }

            vertexSource = vertexBuilder.toString();
            fragmentSource = fragmentBuilder.toString();

            vertShaderID = glCreateShader(GL_VERTEX_SHADER);

            glShaderSource(vertShaderID, vertexSource);
            glCompileShader(vertShaderID);

            int success = glGetShaderi(vertShaderID, GL_COMPILE_STATUS);

            if (success == 0)
            {
                String shaderLog = glGetShaderInfoLog(vertShaderID);
                System.err.println("GLSL ERROR: engine.rendering.Vertex shader compilation failed!\n" + shaderLog);
            }

            fragShaderID = glCreateShader(GL_FRAGMENT_SHADER);

            glShaderSource(fragShaderID, fragmentSource);
            glCompileShader(fragShaderID);

            success = glGetShaderi(fragShaderID, GL_COMPILE_STATUS);

            if (success == 0)
            {
                String shaderLog = glGetShaderInfoLog(fragShaderID);
                System.err.println("GLSL ERROR: Fragment shader compilation failed!\n" + shaderLog);
            }

            shaderProgramID = glCreateProgram();

            glAttachShader(shaderProgramID, vertShaderID);
            glAttachShader(shaderProgramID, fragShaderID);
            glLinkProgram(shaderProgramID);

            success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);

            if (success == 0)
            {
                String programLog = glGetProgramInfoLog(shaderProgramID);
                System.err.println("GLSL ERROR: engine.rendering.Shader linking failed!\n" + programLog);
            }

            glDetachShader(shaderProgramID, vertShaderID);
            glDetachShader(shaderProgramID, fragShaderID);
            glDeleteShader(vertShaderID);
            glDeleteShader(fragShaderID);

            return shaderProgramID;
        }
        catch (Exception e)
        {
            System.err.println("FILE IO ERROR: Could not load specified shader source file!");
            e.printStackTrace();
        }

        return 0;
    }
}