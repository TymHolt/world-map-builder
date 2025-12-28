package org.wmb.loading.obj;

import org.wmb.Log;
import org.wmb.rendering.MeshDataDescription;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ObjTriangleMesh {

    private static final String TAG = "ObjTriangleMesh";

    public final float[] positons;
    public final float[] texturePositions;
    public final float[] normals;
    public final short[] faces;

    public ObjTriangleMesh(List<Float> positions, List<Float> texturePositions, List<Float> normals,
        List<Short> faces) {
        this(toFloatArray(positions), toFloatArray(texturePositions), toFloatArray(normals),
            toShortArray(faces));
    }

    private static float[] toFloatArray(List<Float> floatList) {
        int index = 0;
        final float[] floatArray = new float[floatList.size()];
        for (float value : floatList)
            floatArray[index++] = value;
        return floatArray;
    }

    private static short[] toShortArray(List<Short> shortList) {
        int index = 0;
        final short[] shortArray = new short[shortList.size()];
        for (short value : shortList)
            shortArray[index++] = value;
        return shortArray;
    }

    public ObjTriangleMesh(float[] positons, float[] texturePositions, float[] normals,
        short[] faces) {
        Objects.requireNonNull(positons, "Position array is null");
        Objects.requireNonNull(positons, "Texture position array is null");
        Objects.requireNonNull(positons, "Normal array is null");
        Objects.requireNonNull(positons, "Face array is null");

        if (positons.length % 3 != 0)
            throw new IllegalArgumentException("Position missing coordinate");

        if (texturePositions.length % 2 != 0)
            throw new IllegalArgumentException("Texture position missing coordinate");

        if (normals.length % 3 != 0)
            throw new IllegalArgumentException("Normal missing coordinate");

        if (faces.length % 9 != 0)
            throw new IllegalArgumentException("Face missing index");

        for (int index = 0; index < faces.length;) {
            // OBJ indices start from 1
            final int positionIndex = faces[index++] - 1;
            final int texturePositionIndex = faces[index++] - 1;
            final int normalIndex = faces[index++] - 1;

            if (positionIndex < 0 || positionIndex * 3 >= positons.length) {
                Log.debug(TAG, "Position index: " + positionIndex);
                Log.debug(TAG, "Position count: " + positons.length / 3);
                throw new IllegalArgumentException("Position index out of bounds");
            }

            if (texturePositionIndex < 0 || texturePositionIndex * 2 >= texturePositions.length) {
                Log.debug(TAG, "Texture position index: " + texturePositionIndex);
                Log.debug(TAG, "Texture position count: " + texturePositions.length / 2);
                throw new IllegalArgumentException("Texture position index out of bounds");
            }

            if (normalIndex < 0 || normalIndex * 3 >= normals.length) {
                Log.debug(TAG, "Normal index: " + normalIndex);
                Log.debug(TAG, "Normal count: " + normals.length / 3);
                throw new IllegalArgumentException("Normal index out of bounds");
            }
        }

        this.positons = positons;
        this.texturePositions = texturePositions;
        this.normals = normals;
        this.faces = faces;
    }

    public MeshDataDescription toMeshDataDescription() {
        // TODO This can be stored more efficient, check vertex duplicates etc.
        final List<Float> positionData = new ArrayList<>();
        final List<Float> texturePositionData = new ArrayList<>();
        final List<Float> normalData = new ArrayList<>();
        final List<Short> indexData = new ArrayList<>();

        short vertexIndex = (short) 0;
        for (int index = 0; index < this.faces.length;) {
            // OBJ indices start from 1
            int positionIndex = (faces[index++] - 1) * 3;
            int texturePositionIndex = (faces[index++] - 1) * 2;
            int normalIndex = (faces[index++] - 1) * 3;

            positionData.add(this.positons[positionIndex++]);
            positionData.add(this.positons[positionIndex++]);
            positionData.add(this.positons[positionIndex]);

            texturePositionData.add(this.texturePositions[texturePositionIndex++]);
            texturePositionData.add(this.texturePositions[texturePositionIndex]);

            normalData.add(this.normals[normalIndex++]);
            normalData.add(this.normals[normalIndex++]);
            normalData.add(this.normals[normalIndex]);

            indexData.add(vertexIndex++);
        }

        final MeshDataDescription meshDataDescription = new MeshDataDescription();
        meshDataDescription.addDataArray(3, toFloatArray(positionData));
        meshDataDescription.addDataArray(2, toFloatArray(texturePositionData));
        meshDataDescription.addDataArray(3, toFloatArray(normalData));
        meshDataDescription.setIndexArray(toShortArray(indexData));
        meshDataDescription.verify();
        return meshDataDescription;
    }
}
