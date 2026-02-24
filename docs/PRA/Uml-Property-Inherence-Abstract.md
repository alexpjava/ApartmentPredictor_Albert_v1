# Technical Documentation: Apartment Predictor, a Real Estate Management System — UML v1

```mermaid
---
config:
  layout: elk
---
classDiagram
direction TB

%% --- LEYENDA DEL DIAGRAMA ---
    note for Legend "<b>Simbología UML</b><br/>--<br/><b>Visibilidad</b><br/><b>-</b> Privado<br/><b>+</b> Público<br/><b>#</b> Protegido<br/>--<br/><b>Tipos de Clase</b><br/><b>«abstract»</b> Clase abstracta: no instanciable directamente<br/>--<br/><b>Relaciones</b><br/><b> &lt;|-- </b> Herencia / Extensión<br/><b> &lt;|.. </b> Implementación de Interfaz<br/><b> *-- </b> Composición: el hijo no existe sin el padre<br/><b> o-- </b> Agregación: el hijo puede existir de forma independiente<br/><b> --> </b> Asociación dirigida: referencia unidireccional<br/><b> --  </b> Asociación lógica: relación conceptual bidireccional"
    class Legend {
        <<Leyenda>>
    }

    %% ── CLASES ──────────────────────────────────────────────

    class Person {
        <<abstract>>
        -int id
        -String name
        -String email
        -int age
    }

    class Owner {
        -boolean isActive
        -List~PropertyContract~ contracts
        +register()
        +unregister()
        +sendNotification(String message)
    }

    class Reviewer {
        -List~Review~ reviews
        +addReview(Review review)
        +sendNotification(String message)
        +addFollower(Person p)
        +getFollowersCount()
    }

    class Property {
        <<abstract>>
        -int id
        +double area
        +int locationRating
        +String address
        -boolean airConditioning
        -byte parking
        +int numberOfBedrooms
        +int numberOfBathrooms
        +boolean hasGarden
        +abstract double calculatePrice()
        +abstract String getPropertyType()
        +double calculateAnnualPropertyTax()
        +String listKeyFeatures()
        +boolean isSuitableForFamily(int familySize)
        +double calculateRenovationCost()
        +double estimateRentalIncomePerMonth()
    }

    class Apartment {
        +int floorLevel
        -boolean hasBalcony
        +double calculatePrice()
        +String getPropertyType()
    }

    class School {
        -String id
        -String name
        -String type
        -String location
        -int rating
        -boolean isPublic
        +double calculatePrice()
        +String getPropertyType()
    }

    class PropertyContract {
        -Long id
        +String propertyReferenceCode
        +LocalDate referenceDate
        +float propertyValue
        +float ownershipPercentage
    }

    class Review {
        -Long id
        -int rating
        -String comment
        -LocalDate date
    }

    %% ── RELACIONES ──────────────────────────────────────────

    %% Herencia
    Person <|-- Owner
    Person <|-- Reviewer

    %% Herencia de propiedad
    Property <|-- Apartment
    Property <|-- School

    %% Composición: Apartment agrupa las Schools de su área
    Apartment "1" *-- "0..*" School : schools in area

    %% Composición: Owner posee sus PropertyContracts (mueren con él)
    Owner "1" *-- "0..*" PropertyContract : tiene

    %% Composición: Property posee sus PropertyContracts
    Property "1" *-- "0..*" PropertyContract : referenciada en

    %% Composición: Property posee sus Reviews
    Property "1" *-- "0..*" Review : contiene

    %% Composición: Reviewer posee sus Reviews
    Reviewer "1" *-- "0..*" Review : escribe

    %% Asociación dirigida: PropertyContract apunta a Owner y a Property
    PropertyContract "0..*" --> "1" Owner : pertenece a
    PropertyContract "0..*" --> "1" Property : referencia a

    %% Asociación lógica conceptual Owner <-> Property (via PropertyContract)
    Owner "0..*" -- "0..*" Property : posee (lógico)
```

---

## Notas de diseño — V1

- **`School`** hereda de `Property` y se relaciona con `Apartment` mediante composición (1 Apartment → 0..* Schools). Representa los centros educativos del área de referencia del apartamento, dato clave para la analítica futura (V2).
- **`Person`** es abstracta: no se instancia directamente. `Owner` y `Reviewer` son sus únicas especializaciones en V1.
- **`Property`** es abstracta: el único subtipo concreto en V1 es `Apartment`. Subtipos como `House`, `Duplex` y `Townhouse` se incorporarán en V2.
- **`PropertyContract`** (antes `Ownership`) es la entidad de unión que resuelve la relación muchos-a-muchos entre `Owner` y `Property`, almacenando atributos propios del contrato (valor, porcentaje, fecha).
- **`Review`** es propiedad tanto de `Property` (composición física) como de `Reviewer` (composición autoral). Ambas composiciones son válidas: una `Review` no existe sin su propiedad ni sin su autor.
- Las **interfaces** (`Registrable`, `Notifiable`, `Followable`) y las clases `SingleFamilyHome`, `House`, `Duplex`, `Townhouse` y `Analytics` quedan diferidas para V2.
- La **asociación lógica** `Owner -- Property` se mantiene como referencia conceptual; la persistencia real se gestiona exclusivamente a través de `PropertyContract`.