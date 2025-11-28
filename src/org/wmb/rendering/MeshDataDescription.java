package org.wmb.rendering;

import org.wmb.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MeshDataDescription {

    record DataArray(int elementsPerVertex, float[] data) {

        int vertexDefinitionCount() {
            return data.length / elementsPerVertex;
        }
    }

    private final List<DataArray> dataArrays;
    private short[] indexArray;

    public MeshDataDescription() {
        this.dataArrays = new ArrayList<>();
        this.indexArray = null;
    }

    public void addDataArray(int elementsPerVertex, float[] data) {
        Objects.requireNonNull(data, "Data is null");

        if (elementsPerVertex < 1 || elementsPerVertex > 4)
            throw new IllegalArgumentException("Elements per vertex mus be between 1 and 4");

        if (data.length % elementsPerVertex != 0 || data.length == 0) {
            Log.debug("Data length: " + data.length);
            throw new IllegalArgumentException("Data element/vertex mismatch");
        }

        this.dataArrays.add(new DataArray(elementsPerVertex, data));
    }

    public void setIndexArray(short[] indices) {
        Objects.requireNonNull(indices, "Indices are null");
        this.indexArray = indices;
    }

    public void verify() {
        Objects.requireNonNull(this.indexArray, "Indices are null");

        if (this.dataArrays.isEmpty())
            throw new IllegalStateException("No data array added");

        // All data arrays need to have matching vertex definition count
        final int vertexDefinitionCount = this.dataArrays.getFirst().vertexDefinitionCount();
        for (DataArray dataArray : this.dataArrays)
            if (dataArray.vertexDefinitionCount() != vertexDefinitionCount)
                throw new IllegalArgumentException("Data arrays have different amount of vertices");

        // Indices must be in bounds to the given vertex definitions
        short minIndex = Short.MAX_VALUE;
        short maxIndex = Short.MIN_VALUE;
        for (short index : this.indexArray) {
            if (index < minIndex)
                minIndex = index;
            if (index > maxIndex)
                maxIndex = index;
        }

        if (minIndex < 0 || maxIndex >= vertexDefinitionCount) {
            Log.debug("Index data bounds: min=" + minIndex + " max=" + maxIndex);
            throw new IllegalArgumentException("Index data out of bounds");
        }
    }

    List<DataArray> getDataArrays() {
        return this.dataArrays;
    }

    short[] getIndexArray() {
        return this.indexArray;
    }
}
