package by.bsuir.clotheshop.model.entities.user.gender;

public enum Gender {
    Male("Мужчина"),
    Female("Женщина"),
    TransMale("Трансгендерный мужчина"),
    TransFemale("Трансгендерная женщина"),
    Queer("Квир"),
    NoData("Нет данных");

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
