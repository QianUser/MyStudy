package memory;

import memory.node.Node;
import memory.node.actor.AddActor;
import memory.node.actor.BindActor;
import memory.node.actor.binder.AddNodeBinder;
import memory.node.creater.ChainNodeCreator;
import memory.node.creater.ChildNodeCreator;
import memory.node.creater.FileNodeCreator;
import memory.node.creater.MemoryNodeCreator;
import memory.node.element.StringElement;
import memory.visitor.Visitor;

import java.io.File;
import java.security.MessageDigest;

public class Main {

    public static void main(String[] args) throws Exception {
        Visitor fileReader = new Visitor();
        Visitor memoryReader = new Visitor();
        Visitor memoryWriter = new Visitor();
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        Node root = new MemoryNodeCreator(memoryReader, new File("D:\\记忆"), messageDigest, 0).create();
        Node node = new ChildNodeCreator(
                new ChainNodeCreator(new StringElement("名称"), new StringElement("许景淳")),
                new ChildNodeCreator(
                        new FileNodeCreator(new File("resource\\check\\许景淳\\1987-许景淳专辑"), fileReader),
                        new ChainNodeCreator(new StringElement("来源"), new StringElement("https://pan.baidu.com/s/1hsqrjuc#list/path=%2F"))
                ),
                new ChildNodeCreator(
                        new FileNodeCreator(new File("resource\\check\\许景淳\\00156[162wp]-（台湾百佳012）许景淳专辑（点将唱片）"), fileReader),
                        new ChainNodeCreator(new StringElement("来源"), new StringElement("https://pan.baidu.com/s/1hswkBcw#list/path=%2F"))
                ),
                new ChildNodeCreator(
                        new FileNodeCreator(new File("resource\\check\\许景淳\\许景淳1994-许景淳专辑[玫瑰人生][台湾][WAV整轨].rar"), fileReader),
                        new ChainNodeCreator(new StringElement("来源"), new StringElement("https://306t.com/dir/167219-26347562-56bf1c"))
                ),
                new ChildNodeCreator(
                        new FileNodeCreator(new File("resource\\check\\许景淳\\台湾百佳012-許景淳《玫瑰人生 》滚石[WAV+CUE].rar"), fileReader),
                        new ChainNodeCreator(new StringElement("来源"), new StringElement("https://306t.com/dir/167219-32290046-8a0807"))
                )
        ).create();
        BindActor bindActor = new BindActor();
        node.search(new AddActor(bindActor, root, AddNodeBinder.As.SET_LAST_CHILD, false));
        root.search(bindActor);
    }

}