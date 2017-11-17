package util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import authoring.panels.reserved.MenuBarPanel;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import util.exceptions.XMLException;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * A class that recursively reads in an xml file that defines the menus
 * This class is important because it allows the user to create any number of
 * submenus
 * @author Albert
 *
 * I chose this class as my masterpiece because, while it does not necessarily implement any specific design patterns, I think that
 * this class and this feature is the best way to show that I completely understand the open to extension and closed to modification principle.
 * In essence, this class allows people to create any number of submenus within submenus within submenus, no matter how many levels you want to go down, without
 * changing any kind of code within the actual project besides adding a corresponding class for reflection. I feel that a lot of teams would write code that only reads menus
 * down to a certain level, but this menu reader will read down to any level, which makes it completely reusable.
 *
 * All a user has to do is add an item into the xml file. In addition, this class is completely modular and is completely reusable, because the idea of
 * recursively reading in menus is not restricted to slogo.
 *
 */
public class MenuReader {
    private static final String TYPE_TAG = "type";
    private static final String NAME_TAG = "name";
    private static final String ITEM_TAG = "item";
    private static final String MENU_TAG = "menu";
    private static final String STRATEGY_TAG = "strategy";
    private static final String PREFIX = "frontend.menus.strategies.";

    private Map<String, Menu> mySubMenus;
    Document dom;
    MenuBarPanel bar;

    /**
     * Creates a new MenuReader and reads from file
     * @param path			path of file to read
     * @throws XMLException
     * @throws IOException
     */
    public MenuReader(String path, MenuBarPanel bar) throws XMLException {
        this.bar = bar;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db;
        try {
            db = dbf.newDocumentBuilder();
            dom = db.parse(path);
        } catch (Exception e) {
            throw new RuntimeException("XML file cannot be found");
        }
        readFromFile();
    }

    public Menu[] getMenus(){
        List<Menu> menus = new ArrayList<>();
        for(String s : mySubMenus.keySet()){
            menus.add(mySubMenus.get(s));
        }
        return menus.toArray(new Menu[menus.size()]);
    }

    private void readFromFile() throws XMLException {

        mySubMenus = new LinkedHashMap<>();
        NodeList nList = getNodeList(MENU_TAG);

        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element menu = (Element) nNode;
                parseMenu(menu);
            }
        }
    }

    private NodeList getNodeList(String tag) {
        Element root = dom.getDocumentElement();
        return this.getNodeList(root, tag);
    }

    /**
     * @param e        xml element to read over
     * @param tag    tag to search for
     * @return        List of nodes identifying with that tag
     */
    private NodeList getNodeList(Element e, String tag) {
        NodeList nList = e.getElementsByTagName(tag);
        return nList;
    }

    /**
     * parses the top level menus, which can only be javafx Menus because of the way a MenuBar is set up
     * @param menu
     */
    private void parseMenu(Element menu) throws XMLException{
        Node itemHead = menu.getFirstChild();
        NodeList subItems = menu.getChildNodes();
        String menuName = StringLevelParse(itemHead, subItems.getLength(), NAME_TAG);
        Menu newMenu = parseSubMenu(menu, itemHead, subItems.getLength());
        mySubMenus.put(menuName, newMenu);
    }

    /**
     * Parses through the siblings of a node to find an xml element that matches the given tag
     * @param head		Node that represents head of linkedlist of siblings
     * @param length	length of linkedlist
     * @param tag		tag to search for
     * @return			read in string value whose xml identifier matches tag
     * @throws XMLException
     */
    private String StringLevelParse(Node head, int length, String tag) throws XMLException {
        Node current = head;
        for (int i = 0; i < length; i++) {
            if (current.getNodeType() == Node.ELEMENT_NODE) {
                if (current.getNodeName().equals(tag)) {
                    return current.getTextContent();
                }
            }
            current = current.getNextSibling();
        }

        throw new XMLException();
    }

    /**
     * Creates a SubMenu by recursively reading in all items from the siblings of an initial node
     * @param menu		Element that represents the root menu element in the menubar
     * @param head		head of linkedlist of sibling nodes to read over
     * @param length	length of above linkedlist
     * @return			a recursively defined submenu
     * @throws XMLException
     */
    private Menu parseSubMenu(Element menu, Node head, int length) throws XMLException{
        Menu newMenu = new Menu(StringLevelParse(head, length, NAME_TAG));
        Node current = head;
        for (int j = 0; j < length; j++) {
            if(current == null) {
                continue;
            }

            if (current.getNodeType() == Node.ELEMENT_NODE) {
                Element menuItem = (Element) current;
                if (current.getNodeName().equals(ITEM_TAG)) {
                    MenuItem newItem = createMenuItem(menu, menuItem);
                    newMenu.getItems().add(newItem);
                }
            }
            current = current.getNextSibling();
        }
        return newMenu;
    }

    /**
     * Determines whether a given item shoudld be a menu or a button. If menu, recurse; if button, create button and return
     * @param menu		Element that represents the root menu in the menubar
     * @param menuItem	Element to be created that is either a button or a submenu
     * @return			the correct MenuItem, whether it is button or menu
     * @throws XMLException
     */
    private MenuItem createMenuItem(Element menu, Element menuItem) throws XMLException {
        if (getContent(menuItem, TYPE_TAG).equals(MENU_TAG)) {
            int length = menuItem.getChildNodes().getLength();
            Node head = menuItem.getFirstChild();
            while(head.getNodeType() != Node.ELEMENT_NODE) {
                head = head.getNextSibling();
            }

            Menu subMenu = parseSubMenu(menuItem, head, length);
            return subMenu;
        } else {
            String name = getContent(menuItem, NAME_TAG);
            MenuItem newItem = new MenuItem(name);
            String strategy = getContent(menuItem, STRATEGY_TAG);
            bar.setupItem(newItem, strategy);
            return newItem;
        }
    }

    /**
     * return the first element's text content which is identified by tag
     * @param element
     * @param tag
     * @return
     */
    private String getContent(Element element, String tag) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

    /**
     * @return	a map of menutext to Menus
     */
    public Map<String, Menu> getSubMenus() {
        return mySubMenus;
    }
}
