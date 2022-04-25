package by.bsuir.clotheshop.model.entities.user.gender;

public enum Gender {
    NoData("Нет данных"),
    Male("Мужской"),
    Female("Женский"),
    TransMale("Трансгендерный мужской"),
    TransFemale("Трансгендерная женский"),
    Queer("Квир");

    private final String gender;

    Gender(String gender)
    {
        this.gender = gender;
    }

    @Override
    public String toString()
    {
        return gender;
    }

    public String getGender()
    {
        return gender;
    }
}
