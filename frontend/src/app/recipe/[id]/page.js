"use client";

import { useState, useEffect } from "react";
import { useParams, useRouter } from "next/navigation";

export default function RecipeDetails() {
  const { id } = useParams();
  const router = useRouter();
  const [recipe, setRecipe] = useState(null);
  const [completedStepIds, setCompletedStepIds] = useState([]);
  const [progress, setProgress] = useState(0);

  useEffect(() => {
    fetch(`http://localhost:8081/api/recipes/${id}`)
      .then((res) => {
        if (!res.ok) throw new Error("Recipe not found");
        return res.json();
      })
      .then((data) => {
        // Προσθέτουμε ids στα βήματα αν δεν έχουν, για να δουλεύει το checkbox
        const stepsWithIds = data.steps.map((s, index) => ({
          ...s,
          id: index, // Προσωρινό ID αν το backend δεν στέλνει ID βήματος
        }));
        setRecipe({ ...data, steps: stepsWithIds });
      })
      .catch((err) => {
        console.error(err);
        // alert("Η συνταγή δεν βρέθηκε!"); // Uncomment αν θες
      });
  }, [id]);

  // Υπολογισμός Προόδου
  useEffect(() => {
    if (!recipe) return;
    const completedSteps = recipe.steps.filter((step) =>
      completedStepIds.includes(step.id)
    );
    const completedTime = completedSteps.reduce(
      (sum, step) => sum + (step.duration || 0),
      0
    );
    const totalTime = recipe.totalTime > 0 ? recipe.totalTime : 1;
    let percentage = (completedTime / totalTime) * 100;
    if (percentage > 100) percentage = 100;
    setProgress(Math.round(percentage));
  }, [completedStepIds, recipe]);

  const toggleStep = (stepId) => {
    if (completedStepIds.includes(stepId)) {
      setCompletedStepIds(completedStepIds.filter((id) => id !== stepId));
    } else {
      setCompletedStepIds([...completedStepIds, stepId]);
    }
  };

  if (!recipe) return <div className="text-center mt-10">Φόρτωση...</div>;

  return (
    <div className="min-h-screen bg-gray-50 p-6">
      <div className="max-w-4xl mx-auto bg-white rounded-xl shadow-lg overflow-hidden">
        {/* Header */}
        <div className="bg-blue-600 p-8 text-white relative">
          <button
            onClick={() => router.push("/")}
            className="absolute top-4 left-4 text-white hover:underline"
          >
            ← Πίσω
          </button>
          <h1 className="text-4xl font-bold mb-2">{recipe.name}</h1>
          <div className="flex gap-4 text-blue-100">
            <span>📂 {recipe.category}</span>
            <span>⏱️ {recipe.totalTime} λεπτά</span>
            <span>📊 {recipe.difficulty}</span>
          </div>
        </div>

        <div className="p-8">
          {/* Progress Bar */}
          <div className="mb-8 sticky top-0 bg-white py-4 z-10 border-b">
            <div className="flex justify-between mb-1">
              <span className="font-bold text-gray-700">Πρόοδος</span>
              <span className="font-bold text-blue-600">{progress}%</span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-4">
              <div
                className="bg-green-500 h-4 rounded-full transition-all duration-500"
                style={{ width: `${progress}%` }}
              ></div>
            </div>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-3 gap-8">
            {/* Στήλη 1: Υλικά Συνταγής */}
            <div className="md:col-span-1 bg-orange-50 p-6 rounded-lg h-fit">
              <h3 className="text-xl font-bold mb-4 text-orange-800">
                🛒 Υλικά
              </h3>
              <ul className="space-y-2">
                {recipe.ingredients.map((ing, idx) => (
                  <li
                    key={idx}
                    className="flex items-center gap-2 border-b pb-1 border-orange-200"
                  >
                    <input
                      type="checkbox"
                      className="w-4 h-4 text-orange-600"
                    />
                    <span>
                      {ing.quantity}
                      {ing.unit} <strong>{ing.name}</strong>
                    </span>
                  </li>
                ))}
              </ul>
            </div>

            {/* Στήλη 2: Βήματα */}
            <div className="md:col-span-2">
              <h3 className="text-xl font-bold mb-4 text-gray-800">
                👣 Εκτέλεση
              </h3>
              <div className="space-y-4">
                {recipe.steps.map((step) => (
                  <div
                    key={step.id}
                    onClick={() => toggleStep(step.id)}
                    className={`p-4 border-2 rounded-lg cursor-pointer transition-all ${
                      completedStepIds.includes(step.id)
                        ? "border-green-500 bg-green-50 opacity-80"
                        : "border-gray-200 bg-white"
                    }`}
                  >
                    <div className="flex justify-between items-center mb-2">
                      <h4 className="font-bold text-lg">{step.title}</h4>
                      <span className="bg-gray-100 text-xs font-bold px-2 py-1 rounded">
                        ⏱️ {step.duration}'
                      </span>
                    </div>
                    <p className="text-gray-600 mb-3">{step.description}</p>

                    {/* Φωτογραφία Βήματος (Αλλαγή: photoUrls αντί για stepPhotos) */}
                    {step.photoUrls && step.photoUrls.length > 0 && (
                      <img
                        src={step.photoUrls[0]}
                        alt={step.title}
                        className="w-full h-48 object-cover rounded mb-3"
                      />
                    )}

                    {/* Υλικά Βήματος (Αλλαγή: Είναι λίστα objects τώρα) */}
                    {step.ingredients && step.ingredients.length > 0 && (
                      <div className="bg-yellow-50 p-2 rounded text-sm text-gray-700">
                        <strong>Υλικά βήματος: </strong>
                        {step.ingredients.map((i) => i.name).join(", ")}
                      </div>
                    )}
                  </div>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
