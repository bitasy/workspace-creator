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
 * @author Brian Nieves
 *
 */
public class MenuReader {
    private static final String TYPE_TAG = "type";
    private static final String NAME_TAG = "name";
    private static final String ITEM_TAG = "item";
    private static final String MENU_TAG = "menu";
    private static final String STRATEGY_TAG = "strategy";
    private static final String BUTTON_TAG = "button";

    private Map<String, Menu> mySubMenus;
    private Map<String, MenuItem[]> loads;
    Document dom;
    MenuBarPanel bar;

    /**
     * Creates a new MenuReader and reads from file
     *
     * @param path path of file to read
     * @throws XMLException
     * @throws IOException
     */
    public MenuReader(String path, MenuBarPanel bar) {
        this(path, bar, null);
    }

    public MenuReader(String path, MenuBarPanel bar, Map<String, MenuItem[]> loads) throws XMLException {
        this.loads = loads;
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

    public Menu[] getMenus() {
        List<Menu> menus = new ArrayList<>();
        for (String s : mySubMenus.keySet()) {
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
     * @param e   xml element to read over
     * @param tag tag to search for
     * @return List of nodes identifying with that tag
     */
    private NodeList getNodeList(Element e, String tag) {
        NodeList nList = e.getElementsByTagName(tag);
        return nList;
    }

    /**
     * parses the top level menus, which can only be javafx Menus because of the way a MenuBar is set up
     *
     * @param menu
     */
    private void parseMenu(Element menu) throws XMLException {
        Node itemHead = menu.getFirstChild();
        NodeList subItems = menu.getChildNodes();
        String menuName = StringLevelParse(itemHead, subItems.getLength(), NAME_TAG);
        Menu newMenu = parseSubMenu(menu, itemHead, subItems.getLength());
        mySubMenus.put(menuName, newMenu);
    }

    /**
     * Parses through the siblings of a node to find an xml element that matches the given tag
     *
     * @param head   Node that represents head of linkedlist of siblings
     * @param length length of linkedlist
     * @param tag    tag to search for
     * @throws XMLException
     * @return read in string value whose xml identifier matches tag
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
     *
     * @param menu   Element that represents the root menu element in the menubar
     * @param head   head of linkedlist of sibling nodes to read over
     * @param length length of above linkedlist
     * @throws XMLException
     * @return a recursively defined submenu
     */
    private Menu parseSubMenu(Element menu, Node head, int length) throws XMLException {
        Menu newMenu = new Menu(StringLevelParse(head, length, NAME_TAG));
        Node current = head;
        for (int j = 0; j < length; j++) {
            if (current == null) {
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
     * Determines whether a given item should be a menu or a button. If menu, recurse; if button, create button and return
     *
     * @param menu     Element that represents the root menu in the menubar
     * @param menuItem Element to be created that is either a button or a submenu
     * @throws XMLException
     * @return the correct MenuItem, whether it is button or menu
     */
    private MenuItem createMenuItem(Element menu, Element menuItem) throws XMLException {
        if (getContent(menuItem, TYPE_TAG).equals(MENU_TAG)) {
            int length = menuItem.getChildNodes().getLength();
            Node head = menuItem.getFirstChild();
            while (head.getNodeType() != Node.ELEMENT_NODE) {
                head = head.getNextSibling();
            }

            Menu subMenu = parseSubMenu(menuItem, head, length);
            return subMenu;
        } else if (getContent(menuItem, TYPE_TAG).equals(BUTTON_TAG)) {
            String name = getContent(menuItem, NAME_TAG);
            MenuItem newItem = new MenuItem(name);
            String strategy = getContent(menuItem, STRATEGY_TAG);
            bar.setupItem(newItem, strategy);
            return newItem;
        } else {
            String load = getContent(menuItem, STRATEGY_TAG);
            Menu loadedMenu = new Menu(getContent(menuItem, NAME_TAG));
            loadedMenu.getItems().addAll(loads.get(load));
            return loadedMenu;
        }
    }

    /**
     * return the first element's text content which is identified by tag
     *
     * @param element
     * @param tag
     * @return
     */
    private String getContent(Element element, String tag) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }
}