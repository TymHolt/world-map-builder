package org.wmb.loading.obj;

import org.wmb.Log;

import java.util.ArrayList;
import java.util.List;

public final class ObjFileLoader {

    private static final String TAG = "ObjFileLoader";

    private ObjFileLoader() {

    }

    public static ObjTriangleMesh load(String source) throws ObjFormatException {
        final String[] lines = source.split("\n");
        final List<Float> positions = new ArrayList<>();
        final List<Float> texturePositions = new ArrayList<>();
        final List<Float> normals = new ArrayList<>();
        final List<Integer> faces = new ArrayList<>();

        for (String line : lines) {
            if (line.startsWith("#") || line.isBlank())
                continue;

            final String[] tokens = line.split(" ");
            final String identifier = tokens[0];
            switch (identifier) {
                case "v":
                    readFloatsToList(positions, tokens, 3);
                    break;
                case "vt":
                    readFloatsToList(texturePositions, tokens, 2);
                    break;
                case "vn":
                    readFloatsToList(normals, tokens, 3);
                    break;
                case "f":
                    readFace(faces, tokens);
                    break;
                case "o":
                case "mtllib":
                case "usemtl":
                case "s":
                    Log.info(TAG, "Identifier not implemented: " + identifier);
                    break;
                default:
                    throw new ObjFormatException("Unknown identifier " + identifier);
            }
        }

        // Make sure data is present
        fillFloatList(positions, 0.0f, 3);
        fillFloatList(texturePositions, 0.0f, 2);
        fillFloatList(normals, 0.0f, 3);
        fillIntList(faces, (short) 0, 9);

        try {
            // Constructor checks index integrity
            return new ObjTriangleMesh(positions, texturePositions, normals, faces);
        } catch(IllegalArgumentException exception) {
            throw new ObjFormatException(exception.getMessage());
        }
    }

    private static void readFloatsToList(List<Float> list, String[] tokens, int count)
        throws ObjFormatException {
        if (tokens.length - 1 != count)
            throw new ObjFormatException("Expected " + count + " values");

        try {
            for (int tokenIndex = 1; tokenIndex < tokens.length; tokenIndex++)
                list.add(Float.parseFloat(tokens[tokenIndex]));
        } catch(NumberFormatException exception) {
            throw new ObjFormatException(exception.getMessage());
        }
    }

    private static void readFace(List<Integer> faces, String[] tokens) throws ObjFormatException {
        if (tokens.length - 1 != 3)
            throw new ObjFormatException("Expected 3 vertices");

        for (int tokenIndex = 1; tokenIndex < tokens.length; tokenIndex++)
            readVertex(faces, tokens[tokenIndex].split("/"));
    }

    private static void readVertex(List<Integer> faces, String[] indices) throws ObjFormatException {
        try {
            faces.add(Integer.parseInt(indices[0]));
            faces.add(indices.length > 1 ? Integer.parseInt(indices[1]) : 1);
            faces.add(indices.length > 2 ? Integer.parseInt(indices[2]) : 1);
        } catch(NumberFormatException exception) {
            throw new ObjFormatException(exception.getMessage());
        }
    }

    private static void fillFloatList(List<Float> list, float value, int minListSize) {
        while (list.size() < minListSize)
            list.add(value);
    }

    private static void fillIntList(List<Integer> list, int value, int minListSize) {
        while (list.size() < minListSize)
            list.add(value);
    }
}
