package com.zhicloud.ms.util;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * ClassName: Tree 
 * Function: 定义树父类. 
 * date: Apr 3, 2015 9:06:04 AM 
 *
 * @author sean
 * @version 
 * @since JDK 1.7
 */

public abstract class Tree {
	protected static Log log = LogFactory.getLog(Tree.class);

    private Map<String, TreeNode> treeNodeMaps = new Hashtable<String, TreeNode>();
    private TreeNode root;

    /**
     * root if it's parent is empty
     */
    protected void reload(List<?> nodes) {
        log.info("tree will start reload all data");
        
        synchronized (this) {
            // initialize
            treeNodeMaps.clear();
            root = null;

            List<TreeNode> treeNodes = new Vector<TreeNode>(nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                TreeNode node = this.transform(nodes.get(i)); // transform
                treeNodes.add(node);
                node.setTree(this);
                treeNodeMaps.put(node.getNodeId(), node);
            }
            
            for (int i = 0; i < treeNodes.size(); i++) {
                TreeNode node = (TreeNode) treeNodes.get(i);
                String parentId = node.getParentId();
                if (this.isRootNode(node)) {
                    if (root == null) {
                        root = node;
                    } else {
                        log.error("find more then one root node. ignore.");
                    }
                } else {
                    TreeNode parent = (TreeNode) treeNodeMaps.get(parentId);
                    if (parent != null) {
                        parent.addChild(node);
                        node.setParent(parent);
                    } else {
                        log.warn("node [id=" + node.getNodeId() + "]: missing parent node.");
                    }
                }
            }
        }

        if (root == null) {
            log.error("the root node is not be defined");
        }
    }

    protected boolean isRootNode(TreeNode node) {
        return StringUtils.isBlank(node.getParentId());
    }

    public TreeNode getRootNode() {
        return root;
    }

    public TreeNode getTreeNode(String nodeId) {
        return (TreeNode) treeNodeMaps.get(nodeId);
    }

    public void addTreeNode(TreeNode node) {
        synchronized (this) {
            treeNodeMaps.put(node.getNodeId(), node);

            String parentId = node.getParentId();
            if (StringUtils.isNotBlank(parentId)) {
                TreeNode parent = getTreeNode(parentId);
                if (parent != null) {
                    parent.addChild(node);
                    node.setParent(parent);
                } else {
                    log.error("parent cannot be found: " + node.getParentId());
                }
            } else {
                if (root == null) {
                    root = node;
                } else {
                    log.error("find more then one root node. ignore.");
                }
            }
        }
    }

    public void deleteTreeNode(String nodeId) {
        synchronized (this) {
            TreeNode node = getTreeNode(nodeId);
            if (node == null)
                throw new IllegalArgumentException(nodeId + " cannot be found.");

            if (node.getParent() == null) {
                root = null;
                treeNodeMaps.clear();
                log.warn("the root node has been removed.");
            } else {
                node.getParent().getChildren().remove(node);

                treeNodeMaps.remove(nodeId);
                List<?> children = node.getAllChildren();
                for (int i = 0; i < children.size(); i++) {
                    TreeNode n = (TreeNode) children.get(i);
                    treeNodeMaps.remove(n.getNodeId());
                }
            }
        }
    }

    /**
     * <pre>
     * Usage: Office -&gt;
     * 
     * public TreeNode transform(Object info) {
     *     OfficeInfo office_info = (OfficeInfo) info;
     *     TreeNode node = new TreeNode();
     *     node.setNodeId(office_info.getOfficeId());
     *     node.setParentId(office_info.getParentId());
     *     node.setBindData(office_info);
     *     return node;
     * }
     * </pre>
     */
    protected abstract TreeNode transform(Object info);
}