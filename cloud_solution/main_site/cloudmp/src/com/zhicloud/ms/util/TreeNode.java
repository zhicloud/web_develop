package com.zhicloud.ms.util;

import java.util.List;
import java.util.Vector;

import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.ObjectUtils;

/**
 * 
 * ClassName: TreeNode 
 * Function: 定义节点. 
 * date: Apr 3, 2015 9:11:20 AM 
 *
 * @author sean
 * @version 
 * @since JDK 1.7
 */

public class TreeNode {

	private Tree tree;
    private TreeNode parent;
    private List<TreeNode> children = new Vector<TreeNode>();
    private List<TreeNode> childrenGroup = new Vector<TreeNode>();
    private String nodeId;
    private String parentId;
    private Object bindData;

    public String getNodeId() {
        return nodeId;
    }

    public void setNodeId(String nodeId) {
        this.nodeId = nodeId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Object getBindData() {
        return bindData;
    }

    public void setBindData(Object bindData) {
        this.bindData = bindData;
    }

    public Tree getTree() {
        return tree;
    }

    public void setTree(Tree tree) {
        this.tree = tree;
    }

    public void setParent(TreeNode parent) {
        this.parent = parent;
    }

    public TreeNode getParent() {
        return this.parent;
    }

    public List<TreeNode> getChildren() {
        return this.children;
    }

    public void addChild(TreeNode node) {
        children.add(node);
    }

    /**
     * get all children, and chilren's children
     */
    public List<TreeNode> getAllChildren() {
        if (this.childrenGroup.isEmpty()) {
            synchronized (this.tree) {
                for (int i = 0; i < this.children.size(); i++) {
                    TreeNode node = (TreeNode) this.children.get(i);
                    this.childrenGroup.add(node);
                    this.childrenGroup.addAll(node.getAllChildren());
                }
            }
        }
        return this.childrenGroup;
    }

    /**
     * get all children, and chilren's children
     */
    public List<TreeNode> getAllChildren(Predicate predicate) {
        List<TreeNode> groups = new Vector<TreeNode>();
        fillAllChildren(groups, predicate);
        return groups;
    }

    private void fillAllChildren(List<TreeNode> groups, Predicate predicate) {
        for (int i = 0; i < this.children.size(); i++) {
            TreeNode node = (TreeNode) this.children.get(i);
            if (predicate.evaluate(node)) {
                groups.add(node);
                node.fillAllChildren(groups, predicate);
            }
        }
    }

    /**
     * get all parents, and parent's parent
     */
    public List<TreeNode> getParents() {
        List<TreeNode> results = new Vector<TreeNode>();
        TreeNode parent = this.getParent();
        while (parent != null) {
            results.add(parent);
            parent = parent.getParent();
        }
        return results;
    }

    /**
     * A.isMyParent(B) == B is A' parent ? <br>
     * root.isMyParent(null) == true; <br>
     * root.isMyParent(*) == false <br>
     * *.isMyParent(null) == false
     */
    public boolean isMyParent(String nodeId) {
        TreeNode target = tree.getTreeNode(nodeId);
        TreeNode parent = this.getParent();
        if (parent == null) {
            return target == null;
        } else {
            return parent.equals(target);
        }
    }

    /**
     * A.isMyAncestor(B) == B is A' ancestor ? <br>
     * *.isMyAncestor(null) == true;
     */
    public boolean isMyAncestor(String nodeId) {
        TreeNode target = tree.getTreeNode(nodeId);
        if (target == null)
            return true;

        return target.getAllChildren().contains(this);
    }

    /**
     * A.isMyBrother(B) == B is A' brother ? <br>
     * *.isMyBrother(null) == false
     */
    public boolean isMyBrother(String nodeId) {
        TreeNode target = tree.getTreeNode(nodeId);
        if (target == null)
            return false;

        TreeNode p1 = this.getParent();
        TreeNode p2 = target.getParent();
        return ObjectUtils.equals(p1, p2);
    }
    
}
