package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.w3c.dom.ls.LSOutput;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore mockPrimary;
  private TorpedoStore mockSecondary;

  @BeforeEach
  public void init(){
    this.mockPrimary = mock(TorpedoStore.class);
    this.mockSecondary = mock(TorpedoStore.class);
    this.ship = new GT4500(mockPrimary,mockSecondary);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    //assertEquals(true, result);
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    //assertEquals(true, result);
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(1)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_MultipleShots(){
    // Arrange
    when(mockPrimary.fire(1)).thenReturn(true);
    when(mockSecondary.fire(1)).thenReturn(true);

    // Act
    for (int i = 0; i <4;i++){
      ship.fireTorpedo(FiringMode.SINGLE);
    }

    // Assert
    verify(mockPrimary, times(2)).fire(1);
    verify(mockSecondary, times(2)).fire(1);
  }

  @Test
  public void fireTorpedo_Single_EmptyPrimary(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockPrimary.fire(1)).thenThrow(new IllegalArgumentException());
    when(mockSecondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(1)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_Single_Fail(){
    // Arrange
    when(mockPrimary.fire(1)).thenReturn(false);
    when(mockSecondary.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_Single_EmptyStores(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockPrimary.fire(1)).thenThrow(new IllegalArgumentException());
    when(mockSecondary.isEmpty()).thenReturn(true);
    when(mockSecondary.fire(1)).thenThrow(new IllegalArgumentException());

    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_IsEmptyInvoked(){
    // Arrange
    when(mockPrimary.fire(1)).thenReturn(false);
    when(mockSecondary.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    ship.fireTorpedo(FiringMode.SINGLE);
    ship.fireTorpedo(FiringMode.ALL);

    // Assert
    verify(mockPrimary, times(2)).isEmpty();
    verify(mockSecondary, times(2)).isEmpty();

  }

  @Test
  public void fireTorpedo_Single_EmptySecondary(){
    // Arrange
    when(mockSecondary.isEmpty()).thenReturn(true);
    when(mockSecondary.fire(1)).thenThrow(new IllegalArgumentException());
    when(mockPrimary.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimary, times(2)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_Single_PrimaryEmpties(){
    when(mockSecondary.isEmpty()).thenReturn(true);
    when(mockSecondary.fire(1)).thenThrow(new IllegalArgumentException());
    when(mockPrimary.fire(1)).thenReturn(true);

    // Act
    ship.fireTorpedo(FiringMode.SINGLE);
    when(mockPrimary.isEmpty()).thenReturn(true);
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    verify(mockPrimary, times(1)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
    assertEquals(false, result);
  }

  @Test
  public void fireTorpedo_All_Fail(){
    // Arrange
    when(mockPrimary.isEmpty()).thenReturn(true);
    when(mockPrimary.fire(1)).thenThrow(new IllegalArgumentException());
    when(mockSecondary.isEmpty()).thenReturn(true);
    when(mockSecondary.fire(1)).thenThrow(new IllegalArgumentException());

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
    verify(mockPrimary, times(0)).fire(1);
    verify(mockSecondary, times(0)).fire(1);
  }
}
