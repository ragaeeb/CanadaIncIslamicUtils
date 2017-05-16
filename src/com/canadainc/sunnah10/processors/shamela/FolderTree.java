package com.canadainc.sunnah10.processors.shamela;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class FolderTree<T> implements Iterable<FolderTree<T>>
{
    private T data;
    private FolderTree<T> parent;
    private List<FolderTree<T>> children;

    public FolderTree(T data) {
        this.data = data;
        this.children = new LinkedList<FolderTree<T>>();
    }

    public FolderTree<T> addChild(T child) {
    	FolderTree<T> childNode = new FolderTree<T>(child);
        childNode.parent = this;
        this.children.add(childNode);
        return childNode;
    }
    
    public T getData() {
    	return data;
    }

	@Override
	public Iterator<FolderTree<T>> iterator()
	{
		return null;
	}
}