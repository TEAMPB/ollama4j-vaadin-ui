package agency.schmecker.dev.ollama4j.ui.template;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.RouterLink;

import agency.schmecker.dev.ollama4j.ui.view.ChatView;
import agency.schmecker.dev.ollama4j.ui.view.GenerateView;


public class MainLayout extends AppLayout{

    public MainLayout(){
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        H1 headerTitle = new H1("Ollama4J UI");
        headerTitle.addClassName("header-title");

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(),headerTitle);
        header.setClassName("header");
        header.setWidth("100%");
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        addToNavbar(header);
    }

    private void createDrawer() {
        RouterLink chatRouterLink = new RouterLink("Chat",ChatView.class);
        chatRouterLink.setHighlightCondition(HighlightConditions.sameLocation());

        RouterLink generateRouterLink = new RouterLink("Generate",GenerateView.class);
        chatRouterLink.setHighlightCondition(HighlightConditions.sameLocation());

        addToDrawer(new VerticalLayout(chatRouterLink,generateRouterLink));

    }


    
}
